package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;

public class BookingStateMachineImpl extends AbstractStateMachine<BkgDocumentStatus> implements BookingStateMachine {

  static final DFADefinition<BkgDocumentStatus> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(PENU).successorNodes(REJE, CANC, PENU, PENC)
    .nonTerminalState(PENC).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(CONF).successorNodes(REJE, CMPL, PENU, PENC)
    .terminalStates(CANC, CMPL, REJE)
    .build();

  BookingStateMachineImpl() {
    super(BOOKING_DFA_DEFINITION.fromInitialState());
  }

  BookingStateMachineImpl(BkgDocumentStatus currentStatus) {
    super(BOOKING_DFA_DEFINITION.resumeFromState(currentStatus));
  }

  @Override
  public BkgDocumentStatus getCurrentStatus() {
    return dfa.getCurrentState();
  }

  @Override
  public void cancel() {
    transitionTo(CANC);
  }

  @Override
  public void reject() {
    transitionTo(REJE);
  }

  @Override
  public void pendingUpdate() {
    transitionTo(PENU);
  }

  @Override
  public void pendingConfirmation() {
    transitionTo(PENC);
  }

  @Override
  public void confirm() {
    transitionTo(CONF);
  }

  @Override
  public void complete() {
    transitionTo(CMPL);
  }

  @Override
  protected RuntimeException errorForAttemptLeavingToLeaveTerminalState(BkgDocumentStatus currentState, BkgDocumentStatus successorState, CannotLeaveTerminalStateException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the booking because the booking is "
        + currentState.getValue().toLowerCase() + " (" + currentState.name() + ")",
      e
    );
  }

  @Override
  protected RuntimeException errorForTargetStatNotListedAsSuccessor(BkgDocumentStatus currentState, BkgDocumentStatus successorState, TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the booking with documentStatus ("
        + currentState.name() + ").",
      e
    );
  }

}
