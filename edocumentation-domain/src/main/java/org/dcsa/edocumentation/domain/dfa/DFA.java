package org.dcsa.edocumentation.domain.dfa;

import lombok.Getter;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DFA<S extends Enum<S>> {

  private final DFADefinition<S> definition;

  @Getter
  private S currentState;

  private DFADefinition.DFAStateInfo<S> stateInfo;

  protected DFA(DFADefinition<S> definition, S initialState) {
    this.definition = definition;
    updateStateInfo(initialState, true);
  }

  public void transitionTo(S successor) {
    verifySuccessor(successor);
    updateStateInfo(successor, false);
  }

  private void updateStateInfo(S newState, boolean isInitialState) {
    DFADefinition.DFAStateInfo<S> nextStateInfo = definition.getStateInfoForState(newState);
    // These checks should be redundant as the builder is supposed to have checked this.
    // However, it is nice to have a fail-safe.
    if (nextStateInfo == null) {
      if (isInitialState) {
        throw ConcreteRequestErrorMessageException.internalServerError("Cannot start in unknown state: "
          + this.currentState.name());
      }
      throw ConcreteRequestErrorMessageException.internalServerError("Unknown/Unhandled state: "
        + this.currentState.name());
    }
    if (!nextStateInfo.validState()) {
      if (isInitialState) {
        throw ConcreteRequestErrorMessageException.internalServerError("Invalid starting state: "
          + this.currentState.name() + " (Not a part of the flow)");
      }
      throw ConcreteRequestErrorMessageException.internalServerError("We reached the state "
        + this.currentState.name() + ", which was defined to be unreachable/invalid");
    }
    this.currentState = newState;
    this.stateInfo = nextStateInfo;
  }

  private void verifySuccessor(S intendedSuccessor) {
    Objects.requireNonNull(intendedSuccessor, "successor status cannot be null");
    Set<S> successors = stateInfo.successorStates();
    if (!successors.contains(intendedSuccessor)) {
      String detail = "It is a terminal state!";
      if (!successors.isEmpty()) {
        detail = "The following states are valid: " + successors.stream()
          .map(S::name)
          .collect(Collectors.joining(", ", "[", "]"));
      }
      throw ConcreteRequestErrorMessageException.internalServerError("Invalid transition: "
        + this.currentState.name() + " *CANNOT* got to " + intendedSuccessor.name() + ": " + detail);
    }
  }
}
