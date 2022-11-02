package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.dfa.DFA;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;

public class BookingStateMachineImpl implements BookingStateMachine {

  static final DFADefinition<BkgDocumentStatus> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(PENU).successorNodes(REJE, CANC, PENU, PENC)
    .nonTerminalState(PENC).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(CONF).successorNodes(REJE, CMPL, PENU, PENC)
    .terminalStates(CANC, CMPL, REJE)
    .build();


  private final DFA<BkgDocumentStatus> dfa;

  BookingStateMachineImpl() {
    dfa = BOOKING_DFA_DEFINITION.fromInitialState();
  }

  BookingStateMachineImpl(BkgDocumentStatus currentStatus) {
    dfa = BOOKING_DFA_DEFINITION.resumeFromState(currentStatus);
  }

  @Override
  public BkgDocumentStatus getCurrentStatus() {
    return dfa.getCurrentState();
  }

  @Override
  public void cancel() {
    dfa.transitionTo(CANC);
  }

  @Override
  public void reject() {
    dfa.transitionTo(REJE);
  }

  @Override
  public void pendingUpdate() {
    dfa.transitionTo(PENU);
  }

  @Override
  public void pendingConfirmation() {
    dfa.transitionTo(PENC);
  }

  @Override
  public void confirm() {
    dfa.transitionTo(CONF);
  }

  @Override
  public void complete() {
    dfa.transitionTo(CMPL);
  }

}
