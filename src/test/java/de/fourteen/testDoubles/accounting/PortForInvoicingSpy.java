package de.fourteen.testDoubles.accounting;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class PortForInvoicingSpy
    implements PortForInvoicing {
  private final List<Invoice> invoicedInvoices =
      new ArrayList<>();

  @Override
  public void invoice(final Invoice invoice) {
    invoicedInvoices.add(invoice);
  }

  public Collection<Invoice> invoicedInvoices() {
    return invoicedInvoices;
  }

  @Test
  void some_test() {
    List<Invoice> invoicedInvoices = new ArrayList<>();
    PortForInvoicing portForInvoicing = invoicedInvoices::add;

    assertThat(invoicedInvoices).containsExactly(null /* ... */);
  }
}
