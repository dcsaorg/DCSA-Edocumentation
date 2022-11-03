package org.dcsa.edocumentation.domain.ebl;

import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;

public interface EBLStateMachine {


  /**
   * Transition the document into its {@link EblDocumentStatus#RECE} state.
   */
  void receive();

  /**
   * Transition the document into its {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable
   * in the Amendment flow.</p>
   */
  void pendingUpdate();

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when
   * the EBL flow does not support this state at all. I.e., calling {@link #pendingUpdate()}
   * will trigger an exception causing an internal server error status.</p>
   */
  boolean isPendingUpdateSupported();

  /**
   * Transition the document into its {@link EblDocumentStatus#DRFT} state.
   */
  void draft();

  /**
   * Transition the document into its {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable
   * in the Amendment flow.</p>
   */
  void pendingApproval();

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when
   * the EBL flow does not support this state at all. I.e., calling {@link #pendingApproval()}
   * will trigger an exception causing an internal server error status.</p>
   */
  boolean isPendingApprovalSupported();

  /**
   * Transition the document into its {@link EblDocumentStatus#APPR} state.
   */
  void approve();

  /**
   * Transition the document into its {@link EblDocumentStatus#ISSU} state.
   */
  void issue();

  /**
   * Transition the document into its {@link EblDocumentStatus#SURR} state.
   */
  void surrender();

  /**
   * Transition the document into its {@link EblDocumentStatus#VOID} state.
   */
  // "void" is a keyword and cannot be used as a method name.
  void voidDocument();

  /**
   * @return The current status of the EBL document (SI or TRD)
   */
  EblDocumentStatus getCurrentStatus();

  /**
   * @return true if this state machine is in the amendment flow. Otherwise, false.
   */
  boolean isAmendmentFlow();


  /**
   * Initialize a EBLStateMachine from an initial state in the "default" flow.
   *
   * @return A state machine in the initial state.
   */
  static EBLStateMachine fromInitialStateUsingDefaultFlow() {
    return new EBLStateMachineImpl(false);
  }

  /**
   * Initialize a EBLStateMachine from a stored/serialized state in the "default" flow.
   *
   * @param currentStatus The current document status
   * @return A EBLStateMachine initialized in the given state.
   */
  static EBLStateMachine resumeFromStateUsingDefaultFlow(EblDocumentStatus currentStatus) {
    return new EBLStateMachineImpl(false, currentStatus);
  }



  /**
   * Initialize a EBLStateMachine from an initial state in the "amendment" flow.
   *
   * <p>This flow is used when the document is an amendment to an existing EBL document.</p>
   *
   * @return A state machine in the initial state.
   */
  static EBLStateMachine fromInitialStateUsingAmendmentFlow() {
    return new EBLStateMachineImpl(true);
  }

  /**
   * Initialize a EBLStateMachine from a stored/serialized state in the "amendment" flow.
   *
   * <p>This flow is used when the document is an amendment to an existing EBL document.</p>
   *
   * @param currentStatus The current document status
   * @return A EBLStateMachine initialized in the given state.
   */
  static EBLStateMachine resumeFromStateUsingAmendmentFlow(EblDocumentStatus currentStatus) {
    return new EBLStateMachineImpl(true, currentStatus);
  }
}
