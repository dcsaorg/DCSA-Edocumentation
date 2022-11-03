package org.dcsa.edocumentation.domain.dfa;

public class CannotLeaveTerminalStateException extends InvalidStateTransitionException {

  CannotLeaveTerminalStateException(String message) {
    super(message);
  }
}
