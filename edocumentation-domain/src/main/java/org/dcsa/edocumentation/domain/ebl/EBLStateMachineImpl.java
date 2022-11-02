package org.dcsa.edocumentation.domain.ebl;

import org.dcsa.edocumentation.domain.dfa.DFA;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;

public class EBLStateMachineImpl implements EBLStateMachine {

  static final DFADefinition<EblDocumentStatus> DEFAULT_EBL_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(PENU, DRFT)
    .nonTerminalState(PENU).successorNodes(RECE)
    .nonTerminalState(DRFT).successorNodes(PENA, APPR)
    .nonTerminalState(PENA).successorNodes(DRFT, APPR)
    .nonTerminalState(APPR).successorNodes(ISSU)
    .nonTerminalState(ISSU).successorNodes(SURR)
    .nonTerminalState(SURR).successorNodes(VOID)
    .terminalStates(VOID)
    .build();

  static final DFADefinition<EblDocumentStatus> AMENDMENT_EBL_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(DRFT)
    .nonTerminalState(DRFT).successorNodes(APPR)
    .nonTerminalState(APPR).successorNodes(ISSU)
    .nonTerminalState(ISSU).successorNodes(SURR)
    .nonTerminalState(SURR).successorNodes(VOID)
    .terminalStates(VOID)
    .unreachableStates(PENU, PENA)
    .build();

  private final DFA<EblDocumentStatus> dfa;
  private final boolean isAmendmentFlow;

  EBLStateMachineImpl(boolean isAmendmentFlow) {
    this.isAmendmentFlow = isAmendmentFlow;
    DFADefinition<EblDocumentStatus> definition = fromFlow(isAmendmentFlow);
    this.dfa = definition.fromInitialState();
  }

  EBLStateMachineImpl(boolean isAmendmentFlow, EblDocumentStatus resumeState) {
    this.isAmendmentFlow = isAmendmentFlow;
    DFADefinition<EblDocumentStatus> definition = fromFlow(isAmendmentFlow);
    this.dfa = definition.resumeFromState(resumeState);
  }

  private static DFADefinition<EblDocumentStatus> fromFlow(boolean isAmendmentFlow) {
    return isAmendmentFlow ? AMENDMENT_EBL_DFA_DEFINITION : DEFAULT_EBL_DFA_DEFINITION;
  }

  @Override
  public void receive() {
    dfa.transitionTo(RECE);
  }

  @Override
  public void pendingUpdate() {
    dfa.transitionTo(PENU);
  }

  @Override
  public void draft() {
    dfa.transitionTo(DRFT);
  }

  @Override
  public void pendingApproval() {
    dfa.transitionTo(PENA);
  }

  @Override
  public void approve() {
    dfa.transitionTo(APPR);
  }

  @Override
  public void issue() {
    dfa.transitionTo(ISSU);
  }

  @Override
  public void surrender() {
    dfa.transitionTo(SURR);
  }

  @Override
  public void voidDocument() {
    dfa.transitionTo(VOID);
  }

  @Override
  public EblDocumentStatus getCurrentStatus() {
    return dfa.getCurrentState();
  }

  @Override
  public boolean isAmendmentFlow() {
    return isAmendmentFlow;
  }
}
