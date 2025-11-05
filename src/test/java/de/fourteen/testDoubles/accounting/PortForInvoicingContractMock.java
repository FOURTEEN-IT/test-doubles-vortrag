package de.fourteen.testDoubles.accounting;

import static org.assertj.core.api.Assertions.assertThat;

public final class PortForInvoicingContractMock implements PortForInvoicing {
  @Override
  public void invoice(final Invoice invoice) {
    assertThat(invoice.amount()).isGreaterThanOrEqualTo(0);
  }
}
