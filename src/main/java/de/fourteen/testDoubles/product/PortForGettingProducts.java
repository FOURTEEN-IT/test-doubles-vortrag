package de.fourteen.testDoubles.product;

import java.io.IOException;

public interface PortForGettingProducts {

    void tryToConnect();

    boolean isConnected();

    boolean productExists(int id) throws IOException;

    Product productWithId(int id) throws IOException;
}
