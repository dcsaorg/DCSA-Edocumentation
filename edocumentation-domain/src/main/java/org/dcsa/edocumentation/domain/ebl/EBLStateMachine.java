package org.dcsa.edocumentation.domain.ebl;

import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;

public interface EBLStateMachine {


  /**
   * Transition the document into its {@link EblDocumentStatus#RECE} state.
   */
  void receive();

  /**
   * Transition the document into its {@link EblDocumentStatus#PENU} state.
   */
  void pendingUpdate();

  /**
   * Transition the document into its {@link EblDocumentStatus#DRFT} state.
   */
  void draft();

  /**
   * Transition the document into its {@link EblDocumentStatus#PENA} state.
   */
  void pendingApproval();

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
   * @return true if this state machine is in the amendment flow. Otherwise false.
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
   * This flow is used when the document is an amendment to an existing EBL document.
   *
   * @return A state machine in the initial state.
   */
  static EBLStateMachine fromInitialStateUsingAmendmentFlow() {
    return new EBLStateMachineImpl(true);
  }

  /**
   * Initialize a EBLStateMachine from a stored/serialized state in the "amendment" flow.
   *
   * This flow is used when the document is an amendment to an existing EBL document.
   *
   * @param currentStatus The current document status
   * @return A EBLStateMachine initialized in the given state.
   */
  static EBLStateMachine resumeFromStateUsingAmendmentFlow(EblDocumentStatus currentStatus) {
    return new EBLStateMachineImpl(true, currentStatus);
  }
}
