package de.fourteen.testDoubles.logging;

public final class PortForLoggingDoNothingStub implements PortForLogging {
  @Override
  public void log(final String message) {
    // nothing to do
  }
}
