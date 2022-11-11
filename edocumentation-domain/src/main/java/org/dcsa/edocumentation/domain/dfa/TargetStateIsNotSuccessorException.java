package org.dcsa.edocumentation.domain.dfa;

public class TargetStateIsNotSuccessorException extends InvalidStateTransitionException {

  TargetStateIsNotSuccessorException(String message) {
    super(message);
  }
}
