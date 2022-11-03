package org.dcsa.edocumentation.domain.ebl;

import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.errors.exceptions.ConflictException;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;

public class EBLStateMachineImpl extends AbstractStateMachine<EblDocumentStatus> implements EBLStateMachine {

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

  private final boolean isAmendmentFlow;

  EBLStateMachineImpl(boolean isAmendmentFlow) {
    super(fromFlow(isAmendmentFlow).fromInitialState());
    this.isAmendmentFlow = isAmendmentFlow;
  }

  EBLStateMachineImpl(boolean isAmendmentFlow, EblDocumentStatus resumeState) {
    super(fromFlow(isAmendmentFlow).resumeFromState(resumeState));
    this.isAmendmentFlow = isAmendmentFlow;
  }

  private static DFADefinition<EblDocumentStatus> fromFlow(boolean isAmendmentFlow) {
    return isAmendmentFlow ? AMENDMENT_EBL_DFA_DEFINITION : DEFAULT_EBL_DFA_DEFINITION;
  }

  @Override
  public void receive() {
    transitionTo(RECE);
  }

  @Override
  public void pendingUpdate() {
    transitionTo(PENU);
  }

  @Override
  public boolean isPendingUpdateSupported() {
    return supportsState(PENU);
  }

  @Override
  public void draft() {
    transitionTo(DRFT);
  }

  @Override
  public void pendingApproval() {
    transitionTo(PENA);
  }

  @Override
  public boolean isPendingApprovalSupported() {
    return supportsState(PENA);
  }

  @Override
  public void approve() {
    transitionTo(APPR);
  }

  @Override
  public void issue() {
    transitionTo(ISSU);
  }

  @Override
  public void surrender() {
    transitionTo(SURR);
  }

  @Override
  public void voidDocument() {
    transitionTo(VOID);
  }

  @Override
  protected RuntimeException errorForAttemptLeavingToLeaveTerminalState(EblDocumentStatus currentState, EblDocumentStatus successorState, CannotLeaveTerminalStateException e) {
    // Special-case for terminal states, where we can generate quite nice sounding
    // messages such as "... because the document is void (VOID)".
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the document as the document is "
        + currentState.getValue().toLowerCase() + " (" + currentState.name() + ")",
      e
    );
  }

  @Override
  protected RuntimeException errorForTargetStatNotListedAsSuccessor(EblDocumentStatus currentState, EblDocumentStatus successorState, TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the booking with documentStatus ("
        + currentState.name() + ").",
      e
    );
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
