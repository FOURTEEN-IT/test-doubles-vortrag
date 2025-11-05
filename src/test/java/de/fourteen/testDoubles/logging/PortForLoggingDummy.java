package de.fourteen.testDoubles.logging;

public final class PortForLoggingDummy implements PortForLogging {
  @Override
  public void log(final String message) {
    throw new UnsupportedOperationException("illegal interaction with a dummy");
  }
}
