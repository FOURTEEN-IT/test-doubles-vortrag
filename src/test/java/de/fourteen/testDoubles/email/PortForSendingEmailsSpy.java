package de.fourteen.testDoubles.email;

import de.fourteen.testDoubles.email.Email;
import de.fourteen.testDoubles.email.PortForSendingEmails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class PortForSendingEmailsSpy
    implements PortForSendingEmails {
  private final List<Email> emailsSent = new ArrayList<>();

  @Override
  public void send(final Email email) {
    emailsSent.add(email);
  }

  public Collection<Email> emailsSent() {
    return emailsSent;
  }
}
