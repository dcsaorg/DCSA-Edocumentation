package org.dcsa.edocumentation.domain.dfa;

import lombok.RequiredArgsConstructor;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

@RequiredArgsConstructor
public abstract class AbstractStateMachine<S extends Enum<S>> {

  protected final DFA<S> dfa;

  protected abstract RuntimeException errorForAttemptLeavingToLeaveTerminalState(S currentState, S successorState, CannotLeaveTerminalStateException e);
  protected abstract RuntimeException errorForTargetStatNotListedAsSuccessor(S currentState, S successorState, TargetStateIsNotSuccessorException e);

  public S getCurrentStatus() {
    return dfa.getCurrentState();
  }

  protected boolean supportsState(S state) {
    DFADefinition<S> dfaDefinition = dfa.getDefinition();
    DFAStateInfo<S> stateInfo = dfaDefinition.getStateInfoForState(state);
    return stateInfo != null && stateInfo.validState();
  }

  protected void transitionTo(S state) {
    try {
      dfa.transitionTo(state);
    } catch (CannotLeaveTerminalStateException e) {
      // We always use Conflict because we cannot tell race conditions from bugs here.
      throw errorForAttemptLeavingToLeaveTerminalState(dfa.getCurrentState(), state, e);
    } catch (TargetStateIsNotSuccessorException e) {
      // We always use Conflict because we cannot tell race conditions from bugs here.
      throw errorForTargetStatNotListedAsSuccessor(dfa.getCurrentState(), state, e);
    } catch (InvalidStateTransitionException e) {
      // Catch all for transitions that are never valid (e.g., unknown or unreachable states)
      throw ConcreteRequestErrorMessageException.internalServerError(e.getMessage(), e);
    }
  }
}
