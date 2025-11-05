package de.fourteen.testDoubles.accounting;

@FunctionalInterface
public interface PortForInvoicing {

  /// @param invoice the amount of the invoice must be ≥ 0
  void invoice(Invoice invoice);
}
