package de.fourteen.testDoubles.order;

import java.io.IOException;

interface PortForProcessingOrders {
  void process(Order order) throws IOException;
}
