package org.dcsa.edocumentation.domain.dfa;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DFA<S> {

  @Getter
  private final DFADefinition<S> definition;

  @Getter
  private S currentState;

  private DFAStateInfo<S> stateInfo;

  protected DFA(DFADefinition<S> definition, S initialState) {
    this.definition = definition;
    updateStateInfo(initialState, true);
  }

  public void transitionTo(S successor) {
    verifySuccessor(successor);
    updateStateInfo(successor, false);
  }

  private void updateStateInfo(S newState, boolean isInitialState) {
    DFAStateInfo<S> nextStateInfo = definition.getStateInfoForState(newState);
    // These checks should be redundant as the builder is supposed to have checked this.
    // However, it is nice to have a fail-safe.
    if (nextStateInfo == null) {
      if (isInitialState) {
        throw new UnknownOrUnreachableTargetStateException("Cannot start in unknown state: "
          + this.currentState.toString());
      }
      throw new UnknownOrUnreachableTargetStateException("Unknown/Unhandled state: "
        + this.currentState.toString());
    }
    if (!nextStateInfo.validState()) {
      if (isInitialState) {
        throw new UnknownOrUnreachableTargetStateException("Invalid starting state: "
          + this.currentState.toString() + " (Not a part of the flow)");
      }
      throw new UnknownOrUnreachableTargetStateException("We reached the state "
        + this.currentState.toString() + ", which was defined to be unreachable/invalid");
    }
    this.currentState = newState;
    this.stateInfo = nextStateInfo;
  }

  private void verifySuccessor(S intendedSuccessor) {
    Objects.requireNonNull(intendedSuccessor, "successor status cannot be null");
    Set<S> successors = stateInfo.successorStates();
    if (!successors.contains(intendedSuccessor)) {
      // These errors can happen in normal operations and the service should catch
      // this exception and provide a better (contextual) error message.  Like
      // "It is not possible to change a canceled booking".
      if (successors.isEmpty()) {
        throw new CannotLeaveTerminalStateException("Invalid transition: " + this.currentState.toString()
          + " is a terminal state."
        );
      }
      DFAStateInfo<S> targetStateInfo = definition.getStateInfoForState(intendedSuccessor);
      if (targetStateInfo == null || !targetStateInfo.validState()) {
        if (targetStateInfo != null) {
          throw new UnknownOrUnreachableTargetStateException("The state " + intendedSuccessor.toString()
            + " explicitly declared unreachable.  Verify whether the operation make sense.");
        }
        throw new UnknownOrUnreachableTargetStateException("Cannot transition to unknown/undeclared state "
          + intendedSuccessor.toString() + ". Consider adding the state or explicitly declaring it as unreachable.");
      }
      String detail = "The following states are valid: " + successors.stream()
        .map(S::toString)
        .collect(Collectors.joining(", ", "[", "]"));
      throw new TargetStateIsNotSuccessorException("Invalid transition: "
        + this.currentState.toString() + " *CANNOT* got to " + intendedSuccessor.toString() + ": " + detail);
    }
  }
}
