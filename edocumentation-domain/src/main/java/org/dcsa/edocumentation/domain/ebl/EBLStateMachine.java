package org.dcsa.edocumentation.domain.ebl;

import lombok.Getter;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;

public class EBLStateMachine extends AbstractStateMachine<EblDocumentStatus> {

  private static final DFADefinition<EblDocumentStatus> DEFAULT_EBL_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(PENU, DRFT)
    .nonTerminalState(PENU).successorNodes(RECE)
    .nonTerminalState(DRFT).successorNodes(PENA, APPR)
    .nonTerminalState(PENA).successorNodes(DRFT, APPR)
    .nonTerminalState(APPR).successorNodes(ISSU)
    .nonTerminalState(ISSU).successorNodes(SURR)
    .nonTerminalState(SURR).successorNodes(VOID)
    .terminalStates(VOID)
    .build();

  private static final DFADefinition<EblDocumentStatus> AMENDMENT_EBL_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(DRFT)
    .nonTerminalState(DRFT).successorNodes(APPR)
    .nonTerminalState(APPR).successorNodes(ISSU)
    .nonTerminalState(ISSU).successorNodes(SURR)
    .nonTerminalState(SURR).successorNodes(VOID)
    .terminalStates(VOID)
    .unreachableStates(PENU, PENA)
    .build();

  @Getter
  private final boolean isAmendmentFlow;

  EBLStateMachine(boolean isAmendmentFlow) {
    super(fromFlow(isAmendmentFlow).fromInitialState());
    this.isAmendmentFlow = isAmendmentFlow;
  }

  EBLStateMachine(boolean isAmendmentFlow, EblDocumentStatus resumeState) {
    super(fromFlow(isAmendmentFlow).resumeFromState(resumeState));
    this.isAmendmentFlow = isAmendmentFlow;
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#RECE} state.
   */
  public void receive() {
    transitionTo(RECE);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable
   * in the Amendment flow.</p>
   */
  public void pendingUpdate() {
    transitionTo(PENU);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when
   * the EBL flow does not support this state at all. I.e., calling {@link #pendingUpdate()}
   * will trigger an exception causing an internal server error status.</p>
   */
  public boolean isPendingUpdateSupported() {
    return supportsState(PENU);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#DRFT} state.
   */
  public void draft() {
    transitionTo(DRFT);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable
   * in the Amendment flow.</p>
   */
  public void pendingApproval() {
    transitionTo(PENA);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when
   * the EBL flow does not support this state at all. I.e., calling {@link #pendingApproval()}
   * will trigger an exception causing an internal server error status.</p>
   */
  public boolean isPendingApprovalSupported() {
    return supportsState(PENA);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#APPR} state.
   */
  public void approve() {
    transitionTo(APPR);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#ISSU} state.
   */
  public void issue() {
    transitionTo(ISSU);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#SURR} state.
   */
  public void surrender() {
    transitionTo(SURR);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#VOID} state.
   */
  // "void" is a keyword and cannot be used as a method name.
  public void voidDocument() {
    transitionTo(VOID);
  }

  /**
   * Initialize a EBLStateMachine from an initial state in the "default" flow.
   *
   * @return A state machine in the initial state.
   */
  public static EBLStateMachine fromInitialStateUsingDefaultFlow() {
    return new EBLStateMachine(false);
  }

  /**
   * Initialize a EBLStateMachine from a stored/serialized state in the "default" flow.
   *
   * @param currentStatus The current document status
   * @return A EBLStateMachine initialized in the given state.
   */
  public static EBLStateMachine resumeFromStateUsingDefaultFlow(EblDocumentStatus currentStatus) {
    return new EBLStateMachine(false, currentStatus);
  }

  /**
   * Initialize a EBLStateMachine from an initial state in the "amendment" flow.
   *
   * <p>This flow is used when the document is an amendment to an existing EBL document.</p>
   *
   * @return A state machine in the initial state.
   */
  public static EBLStateMachine fromInitialStateUsingAmendmentFlow() {
    return new EBLStateMachine(true);
  }

  /**
   * Initialize a EBLStateMachine from a stored/serialized state in the "amendment" flow.
   *
   * <p>This flow is used when the document is an amendment to an existing EBL document.</p>
   *
   * @param currentStatus The current document status
   * @return A EBLStateMachine initialized in the given state.
   */
  public static EBLStateMachine resumeFromStateUsingAmendmentFlow(EblDocumentStatus currentStatus) {
    return new EBLStateMachine(true, currentStatus);
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

  private static DFADefinition<EblDocumentStatus> fromFlow(boolean isAmendmentFlow) {
    return isAmendmentFlow ? AMENDMENT_EBL_DFA_DEFINITION : DEFAULT_EBL_DFA_DEFINITION;
  }

}
