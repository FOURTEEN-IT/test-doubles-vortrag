package de.fourteen.testDoubles.order;

import de.fourteen.testDoubles.accounting.Invoice;
import de.fourteen.testDoubles.accounting.PortForInvoicing;
import de.fourteen.testDoubles.accounting.PortForInvoicingSpy;
import de.fourteen.testDoubles.customer.PortForGettingCustomerData;
import de.fourteen.testDoubles.customer.PortForGettingCustomerDataStub;
import de.fourteen.testDoubles.email.Email;
import de.fourteen.testDoubles.email.PortForSendingEmails;
import de.fourteen.testDoubles.email.PortForSendingEmailsSpy;
import de.fourteen.testDoubles.logging.PortForLogging;
import de.fourteen.testDoubles.logging.PortForLoggingDoNothingStub;
import de.fourteen.testDoubles.product.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

final class ProcessingOrdersUseCaseTest {
  @Test
  void it_only_works_if_connected_to_PortForGettingProducts() {
    PortForGettingProducts portForGettingProducts = new NotConnectedStub();
    PortForLogging portForLogging = null; // ?
    PortForGettingCustomerData portForGettingCustomerData = null; // ?
    PortForSendingEmails portForSendingEmails = null; // ?
    PortForInvoicing portForInvoicing = null; // ?
    ProcessingOrdersUseCase sut = new ProcessingOrdersUseCase(
        portForGettingProducts,
        portForLogging,
        portForGettingCustomerData,
        portForSendingEmails,
        portForInvoicing
    );
    Order order = null; // ?

    Throwable thrownException = catchThrowable(() -> sut.process(order));

    assertThat(thrownException)
        .as("thrown exception")
        .isInstanceOf(IOException.class)
        .hasMessage("PortForGettingPorducts is not connected");
  }

  @Test
  void end_to_end_test() throws IOException {
    PortForGettingProducts portForGettingProducts =
        new PortForGettingProductsFake(
            List.of(
                new Product(1, null, 20),
                new Product(2, null, 10),
                new Product(3, null, 50)
            ));
    PortForLogging portForLogging = new PortForLoggingDoNothingStub();
    PortForGettingCustomerDataStub portForGettingCustomerData =
        new PortForGettingCustomerDataStub();
    String emailAddress = "test@test.de";
    portForGettingCustomerData.stubEmailAddressByCustomerId(emailAddress);
    PortForSendingEmailsSpy portForSendingEmails =
        new PortForSendingEmailsSpy();
    PortForInvoicingSpy portForInvoicing = new PortForInvoicingSpy();
    ProcessingOrdersUseCase sut = new ProcessingOrdersUseCase(
        portForGettingProducts,
        portForLogging,
        portForGettingCustomerData,
        portForSendingEmails,
        portForInvoicing
    );
    int orderId = 1;
    int customerId = 1;
    Order order = new Order(
        orderId,
        customerId,
        new OrderPosition(1, 4),
        new OrderPosition(2, 7),
        new OrderPosition(3, 2)
    );

    sut.process(order);

    int expectedAmount = 4 * 20 + 7 * 10 + 2 * 50;
    Invoice expectedInvoice = new Invoice(expectedAmount, customerId);
    Email expectedEmail = new Email(
        "Your order has been processed!",
        "Your order with ID %s has been processed and will arrive shortly."
            .formatted(orderId),
        emailAddress
    );
    assertAll(
        () -> assertThat(portForSendingEmails.emailsSent())
            .containsExactly(expectedEmail),
        () -> assertThat(portForInvoicing.invoicedInvoices())
            .containsExactly(expectedInvoice)
    );
  }

  @Test
  void end_to_end_test_mockito() throws IOException {
    PortForGettingProducts portForGettingProducts =
        PortForGettingProductsFakeMitMockito.fake(
            List.of(
                new Product(1, null, 20),
                new Product(2, null, 10),
                new Product(3, null, 50)
            ));
    PortForLogging portForLogging = mock(PortForLogging.class);
    PortForGettingCustomerData portForGettingCustomerData =
        mock(PortForGettingCustomerData.class);
    String emailAddress = "test@test.de";
    when(portForGettingCustomerData.emailAddressByCustomerId(anyInt())).thenReturn(
        emailAddress);
    PortForSendingEmails portForSendingEmails =
        mock(PortForSendingEmails.class);
    PortForInvoicing portForInvoicing = mock(PortForInvoicing.class);
    ProcessingOrdersUseCase sut = new ProcessingOrdersUseCase(
        portForGettingProducts,
        portForLogging,
        portForGettingCustomerData,
        portForSendingEmails,
        portForInvoicing
    );
    int orderId = 1;
    int customerId = 1;
    Order order = new Order(
        orderId,
        customerId,
        new OrderPosition(1, 4),
        new OrderPosition(2, 7),
        new OrderPosition(3, 2)
    );

    sut.process(order);

    int expectedAmount = 4 * 20 + 7 * 10 + 2 * 50;
    Invoice expectedInvoice = new Invoice(expectedAmount, customerId);
    Email expectedEmail = new Email(
        "Your order has been processed!",
        "Your order with ID %s has been processed and will arrive shortly."
            .formatted(orderId),
        emailAddress
    );
    assertAll(
        () -> {
          verify(portForSendingEmails).send(expectedEmail);
          verifyNoMoreInteractions(portForSendingEmails);
        },
        () -> {
          verify(portForInvoicing).invoice(expectedInvoice);
          verifyNoMoreInteractions(portForInvoicing);
        }
    );
  }

  @Test
  void end_to_end_test_mockito_interaction_based() throws IOException {
    PortForGettingProducts portForGettingProducts =
        mock(PortForGettingProducts.class);
    when(portForGettingProducts.isConnected()).thenReturn(true);
    when(portForGettingProducts.productExists(1)).thenReturn(true);
    when(portForGettingProducts.productExists(2)).thenReturn(true);
    when(portForGettingProducts.productExists(3)).thenReturn(true);
    when(portForGettingProducts.productWithId(1))
        .thenReturn(new Product(1, null, 20));
    when(portForGettingProducts.productWithId(2))
        .thenReturn(new Product(2, null, 10));
    when(portForGettingProducts.productWithId(3))
        .thenReturn(new Product(3, null, 50));
    PortForLogging portForLogging = mock(PortForLogging.class);
    PortForGettingCustomerData portForGettingCustomerData =
        mock(PortForGettingCustomerData.class);
    String emailAddress = "test@test.de";
    when(portForGettingCustomerData.emailAddressByCustomerId(anyInt())).thenReturn(
        emailAddress);
    PortForSendingEmails portForSendingEmails =
        mock(PortForSendingEmails.class);
    PortForInvoicing portForInvoicing = mock(PortForInvoicing.class);
    ProcessingOrdersUseCase sut = new ProcessingOrdersUseCase(
        portForGettingProducts,
        portForLogging,
        portForGettingCustomerData,
        portForSendingEmails,
        portForInvoicing
    );
    int orderId = 1;
    int customerId = 1;
    Order order = new Order(
        orderId,
        customerId,
        new OrderPosition(1, 4),
        new OrderPosition(2, 7),
        new OrderPosition(3, 2)
    );

    sut.process(order);

    int expectedAmount = 4 * 20 + 7 * 10 + 2 * 50;
    Invoice expectedInvoice = new Invoice(expectedAmount, customerId);
    Email expectedEmail = new Email(
        "Your order has been processed!",
        "Your order with ID %s has been processed and will arrive shortly."
            .formatted(orderId),
        emailAddress
    );
    assertAll(
        () -> verify(portForGettingProducts).tryToConnect(),
        () -> verify(portForGettingProducts).isConnected(),
        () -> {
          verify(portForSendingEmails).send(expectedEmail);
          verifyNoMoreInteractions(portForSendingEmails);
        },
        () -> {
          verify(portForInvoicing).invoice(expectedInvoice);
          verifyNoMoreInteractions(portForInvoicing);
        }
    );
  }
}
