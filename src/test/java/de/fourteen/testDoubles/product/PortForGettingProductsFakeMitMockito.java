package de.fourteen.testDoubles.product;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;

public final class PortForGettingProductsFakeMitMockito {
  public static PortForGettingProducts fake(List<Product> products) throws IOException {
    final boolean[] isConnected = {false};
    PortForGettingProducts fake = mock(PortForGettingProducts.class);
    doAnswer(invocationOnMock -> {
      isConnected[0] = true;
      return null;
    }).when(fake).tryToConnect();
    doAnswer(invocationOnMock -> isConnected[0]).when(fake).isConnected();
    doAnswer(
        invocationOnMock -> {
          if (!isConnected[0])  throw new IOException("not connected");
          int id = invocationOnMock.getArgument(0);
          return products.stream().anyMatch(product -> product.id() == id);
        }
    ).when(fake).productExists(anyInt());
    doAnswer(
        invocationOnMock -> {
          if (!isConnected[0]) throw new IOException("not connected");
          int id = invocationOnMock.getArgument(0);
          return products
              .stream()
              .filter(product -> product.id() == id)
              .findAny()
              .orElseThrow(NoSuchElementException::new);
        }
    ).when(fake).productWithId(anyInt());
    return fake;
  }
}
