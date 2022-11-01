package org.dcsa.edocumentation.domain.booking;

import lombok.Getter;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingStateMachineImpl implements BookingStateMachine {

  @Getter
  BkgDocumentStatus currentStatus;

  BookingStateMachineImpl() {
    currentStatus = BookingStateMachineTables.INITIAL_STATE;
  }

  BookingStateMachineImpl(BkgDocumentStatus currentStatus) {
    this.currentStatus = Objects.requireNonNull(currentStatus, "currentStatus cannot be null");
    verifyState(currentStatus);
  }

  private void transitionTo(BkgDocumentStatus successor) {
    verifyTransition(successor);
    currentStatus = successor;
  }

  void verifyTransition(BkgDocumentStatus intendedSuccessor) {
    Objects.requireNonNull(intendedSuccessor, "successor status cannot be null");
    Set<BkgDocumentStatus> successors = BookingStateMachineTables.BOOKING_STATE_TRANSITION_TABLE.get(this.currentStatus);
    if (successors == null) {
      throw ConcreteRequestErrorMessageException.internalServerError("Unknown/Unhandled state: "
        + this.currentStatus.name());
    }
    if (!successors.contains(intendedSuccessor)) {
      String detail = "It is a terminal state!";
      if (!successors.isEmpty()) {
        detail = "The following states are valid: " + successors.stream()
          .map(BkgDocumentStatus::name)
          .collect(Collectors.joining(", ", "[", "]"));
      }
      throw ConcreteRequestErrorMessageException.internalServerError("Invalid transition: "
        + this.currentStatus.name() + " *CANNOT* got to " + intendedSuccessor.name() + ": " + detail);
    }
  }

  @Override
  public void cancel() {
    this.transitionTo(BkgDocumentStatus.CANC);
  }

  @Override
  public void reject() {
    this.transitionTo(BkgDocumentStatus.REJE);
  }

  @Override
  public void pendingUpdate() {
    this.transitionTo(BkgDocumentStatus.PENU);
  }

  @Override
  public void pendingConfirmation() {
    this.transitionTo(BkgDocumentStatus.PENC);
  }

  @Override
  public void confirm() {
    this.transitionTo(BkgDocumentStatus.CONF);
  }

  @Override
  public void complete() {
    this.transitionTo(BkgDocumentStatus.CMPL);
  }

  private static void verifyState(BkgDocumentStatus status) {
    if (!BookingStateMachineTables.BOOKING_STATE_TRANSITION_TABLE.containsKey(status)) {
      throw ConcreteRequestErrorMessageException.internalServerError("Invalid state: " + status.name()
        + " is not in the transition table");
    }
  }
}
