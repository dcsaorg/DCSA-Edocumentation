package org.dcsa.edocumentation.domain.dfa;

public class InvalidStateTransitionException extends RuntimeException {

  InvalidStateTransitionException(String message) {
    super(message);
  }
}
