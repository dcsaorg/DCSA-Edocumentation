package org.dcsa.edocumentation.domain.dfa;

import lombok.AccessLevel;
import lombok.Getter;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import jakarta.persistence.Transient;

public abstract class AbstractStateMachine<S> {

  protected abstract RuntimeException errorForAttemptToLeaveTerminalState(S currentState, S successorState, CannotLeaveTerminalStateException e);
  protected abstract RuntimeException errorForTargetStateNotListedAsSuccessor(S currentState, S successorState, TargetStateIsNotSuccessorException e);

  @Getter(AccessLevel.PROTECTED)
  @Transient
  private DFA<S> dfa;

  /**
   *
   * @return The DFA definition used to answer what the DFA can support but also
   *         used for bootstrapping the DFA on the first transition.  The
   *         implementation is expected to return the same {@link DFADefinition}
   *         every time for correct behaviour.
   */
  protected abstract DFADefinition<S> getDfaDefinition();

  /**
   *
   * @return The state this state machine should resume from on its first transition.
   *         If null, then the state machine will start from the initial state.
   */
  protected abstract S getResumeFromState();

  protected void handleInitialTransition(S successorState) {
    DFADefinition<S> dfaDefinition = getDfaDefinition();
    S resumeState = getResumeFromState();
    if (resumeState == null && successorState == dfaDefinition.initialState) {
      /* We special case the initial transition because of how we build the DFA.
       * We are bound by the states of the DCSA defined standard and cannot create
       * a "custom" start state.
       *
       * We have a gap where you really want to start with calling ".receive()" on
       * the Booking/ShippingInstruction to generate the initial ShipmentEvent but
       * (without this special handling) you would be unable to do so as
       * RECEIVED -> RECEIVED is not a valid transition in general (i.e., creating the DFA
       * in the initialState (RECEIVED) and then calling transitionTo(RECEIVED) would
       * trigger an exception).
       *
       * In a "normal" DFA, you would just have a distinct starting state for this
       * case, but would require us to have another "fake" value for in all the
       * relevant state machine enumerations
       */
      dfa = dfaDefinition.fromInitialState();
    } else {
      this.dfa = dfaDefinition.resumeFromState(resumeState);
      dfa.transitionTo(successorState);
    }
  }

  protected boolean supportsState(S state) {
    DFADefinition<S> dfaDefinition = getDfaDefinition();
    DFAStateInfo<S> stateInfo = dfaDefinition.getStateInfoForState(state);
    return stateInfo != null && stateInfo.validState();
  }

  protected void transitionTo(S state) {
    try {
      if (dfa == null) {
        handleInitialTransition(state);
      } else {
        dfa.transitionTo(state);
      }
    } catch (CannotLeaveTerminalStateException e) {
      // We always use Conflict because we cannot tell race conditions from bugs here.
      throw errorForAttemptToLeaveTerminalState(dfa.getCurrentState(), state, e);
    } catch (TargetStateIsNotSuccessorException e) {
      // We always use Conflict because we cannot tell race conditions from bugs here.
      throw errorForTargetStateNotListedAsSuccessor(dfa.getCurrentState(), state, e);
    } catch (InvalidStateTransitionException e) {
      // Catch all for transitions that are never valid (e.g., unknown or unreachable states)
      throw ConcreteRequestErrorMessageException.internalServerError(e.getMessage(), e);
    }
  }
}
