package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;

/**
 * Booking State Machine
 *
 * <pre>{@code
 *    BookingStateMachine stateMachine = BookingStateMachine.startFromInitialState();
 *    stateMachine = stateMachine.pendingUpdate();
 *    assert stateMachine.getCurrentStatus() == BkgDocumentStatus.PENU;
 *    assert stateMachine.getRememberedStatus() == BkgDocumentStatus.PENC;
 *
 *    // Imagine a save/load cycle here
 *
 *    BookingStateMachine stateMachine = BookingStateMachine.startFromExistingState(
 *        BkgDocumentStatus.PENU,
 *        BkgDocumentStatus.PENC
 *    )
 *    assert stateMachine.acceptDocumentChange().getCurrentStatus() == BkgDocumentStatus.PENC;
 * }</pre>
 */
public interface BookingStateMachine {

  /**
   * Transition the booking into its {@link BkgDocumentStatus#CANC} state.
   */
  void cancel();

  /**
   * Transition the booking into its {@link BkgDocumentStatus#REJE} state.
   */
  void reject();

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENU} state.
   */
  void pendingUpdate();

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  void pendingConfirmation();

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  void confirm();

  /**
   * Transition the booking into its {@link BkgDocumentStatus#CMPL} state.
   */
  void complete();

  /**
   * @return The current status of the booking document
   */
  BkgDocumentStatus getCurrentStatus();

  /**
   * Initialize a BookingStateMachine from an initial state.
   *
   * @return A state machine in the initial state.
   */
  static BookingStateMachine fromInitialState() {
    return new BookingStateMachineImpl();
  }

  /**
   * Initialize a BookingStateMachine from a stored/serialized state.
   *
   * @param currentStatus The current document status
   * @return A BookingStateMachine initialized in the given state.
   */
  static BookingStateMachine resumeFromState(BkgDocumentStatus currentStatus) {
    return new BookingStateMachineImpl(currentStatus);
  }

}
