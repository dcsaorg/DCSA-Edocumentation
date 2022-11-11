package org.dcsa.edocumentation.domain.dfa;

public class UnknownOrUnreachableTargetStateException extends InvalidStateTransitionException {

  UnknownOrUnreachableTargetStateException(String message) {
    super(message);
  }
}
