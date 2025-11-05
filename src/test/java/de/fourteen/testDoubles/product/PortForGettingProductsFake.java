package de.fourteen.testDoubles.product;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public final class PortForGettingProductsFake
    implements PortForGettingProducts {
  private final List<Product> products;
  private boolean isConnected = false;

  public PortForGettingProductsFake(final List<Product> products) {
    this.products = products;
  }

  @Override
  public void tryToConnect() {
    isConnected = true;
  }

  @Override
  public boolean isConnected() {
    return isConnected;
  }

  @Override
  public boolean productExists(final int id) throws IOException {
//    if (!isConnected()) throw new IOException("not connected");
    return products.stream().anyMatch(product -> product.id() == id);
  }

  @Override
  public Product productWithId(final int id) throws IOException {
    if (!isConnected()) throw new IOException("not connected");
    return products
        .stream()
        .filter(product -> product.id() == id)
        .findAny()
        .orElseThrow(NoSuchElementException::new);
  }
}
