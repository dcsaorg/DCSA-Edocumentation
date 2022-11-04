package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;

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
public class BookingStateMachine extends AbstractStateMachine<BkgDocumentStatus> {

  private static final DFADefinition<BkgDocumentStatus> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(PENU).successorNodes(REJE, CANC, PENU, PENC)
    .nonTerminalState(PENC).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(CONF).successorNodes(REJE, CMPL, PENU, PENC)
    .terminalStates(CANC, CMPL, REJE)
    .build();

  BookingStateMachine() {
    super(BOOKING_DFA_DEFINITION.fromInitialState());
  }

  BookingStateMachine(BkgDocumentStatus currentStatus) {
    super(BOOKING_DFA_DEFINITION.resumeFromState(currentStatus));
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#CANC} state.
   */
  public void cancel() {
    transitionTo(CANC);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#REJE} state.
   */
  public void reject() {
    transitionTo(REJE);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENU} state.
   */
  public void pendingUpdate() {
    transitionTo(PENU);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  public void pendingConfirmation() {
    transitionTo(PENC);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  public void confirm() {
    transitionTo(CONF);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#CMPL} state.
   */
  public void complete() {
    transitionTo(CMPL);
  }

  /**
   * Initialize a BookingStateMachine from an initial state.
   *
   * @return A state machine in the initial state.
   */
  static BookingStateMachine fromInitialState() {
    return new BookingStateMachine();
  }

  /**
   * Initialize a BookingStateMachine from a stored/serialized state.
   *
   * @param currentStatus The current document status
   * @return A BookingStateMachine initialized in the given state.
   */
  static BookingStateMachine resumeFromState(BkgDocumentStatus currentStatus) {
    return new BookingStateMachine(currentStatus);
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
