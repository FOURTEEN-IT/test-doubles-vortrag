package de.fourteen.testDoubles.customer;

public final class PortForGettingCustomerDataStub
    implements PortForGettingCustomerData {

  private String stubbedEmailAddressByCustomerId = "";

  @Override
  public String emailAddressByCustomerId(final int customerId) {
    return stubbedEmailAddressByCustomerId;
  }

  public void stubEmailAddressByCustomerId(
      String stubbedEmailAddressByCustomerId) {
    this.stubbedEmailAddressByCustomerId =
        stubbedEmailAddressByCustomerId;
  }
}
