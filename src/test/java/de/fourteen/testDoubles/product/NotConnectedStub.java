package de.fourteen.testDoubles.product;

import java.io.IOException;

public final class NotConnectedStub implements PortForGettingProducts {
  @Override
  public void tryToConnect() {
    // connecting is not successful
  }

  @Override
  public boolean isConnected() {
    return false;
  }

  @Override
  public boolean productExists(final int id) throws IOException {
    throw new IOException("not connected");
  }

  @Override
  public Product productWithId(final int id) throws IOException {
    throw new IOException("not connected");
  }
}
