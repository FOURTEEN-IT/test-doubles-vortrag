package de.fourteen.testDoubles.order;

import de.fourteen.testDoubles.accounting.Invoice;
import de.fourteen.testDoubles.accounting.PortForInvoicing;
import de.fourteen.testDoubles.customer.PortForGettingCustomerData;
import de.fourteen.testDoubles.email.Email;
import de.fourteen.testDoubles.email.PortForSendingEmails;
import de.fourteen.testDoubles.logging.PortForLogging;
import de.fourteen.testDoubles.product.PortForGettingProducts;
import de.fourteen.testDoubles.product.Product;

import java.io.IOException;
import java.util.NoSuchElementException;

record ProcessingOrdersUseCase(
    PortForGettingProducts portForGettingProducts,
    PortForLogging portForLogging,
    PortForGettingCustomerData portForGettingCustomerData,
    PortForSendingEmails portForSendingEmails,
    PortForInvoicing portForInvoicing
) implements PortForProcessingOrders {
  @Override
  public void process(final Order order) throws IOException {
    connectToPortForGettingProducts();
    int totalAmount = totalAmount(order);
    sendConfirmationEmail(order);
    invoice(order.customerId(), totalAmount);
  }

  private void connectToPortForGettingProducts() throws IOException {
    portForGettingProducts.tryToConnect();
    if (!portForGettingProducts.isConnected()) {
      throw new IOException("PortForGettingPorducts is not connected");
    }
  }

  private int totalAmount(final Order order) throws IOException {
    int totalAmount = 0;
    for (OrderPosition orderPosition : order.orderPositions()) {
      if (!portForGettingProducts.productExists(orderPosition.productId())) {
        throw new NoSuchElementException(
            "product with id %s does not exist".formatted(orderPosition.productId()));
      }
      Product product = portForGettingProducts.productWithId(orderPosition.productId());
      portForLogging.log("Product: " + product.name());
      int priceOfOrderPosition = product.price() * orderPosition.count();
      totalAmount += priceOfOrderPosition;
    }
    portForLogging.log("Total Amount: " + totalAmount);
    return totalAmount;
  }

  private void sendConfirmationEmail(final Order order) {
    String customerEmailAddress = portForGettingCustomerData
        .emailAddressByCustomerId(order.customerId());
    portForSendingEmails.send(new Email(
        "Your order has been processed!",
        "Your order with ID %s has been processed and will arrive shortly."
            .formatted(order.id()),
        customerEmailAddress
    ));
  }

  private void invoice(final int customerId, final int totalAmount) {
    portForInvoicing.invoice(new Invoice(totalAmount, customerId));
  }
}
