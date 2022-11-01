package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.InternalServerErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;


class BookingStateMachineTest {

  @Test
  void testBasic() {
    BookingStateMachine bookingStateMachine = BookingStateMachine.startFromInitialState();
    Assertions.assertEquals(RECE, bookingStateMachine.getCurrentStatus());
    bookingStateMachine.pendingConfirmation();
    Assertions.assertEquals(PENC, bookingStateMachine.getCurrentStatus());
    bookingStateMachine.pendingUpdate();
    Assertions.assertEquals(PENU, bookingStateMachine.getCurrentStatus());
  }

  @Test
  void loadInvalidStateTransitions() {
    BkgDocumentStatus[] terminalStates = {
      CANC,
      REJE,
      CMPL
    };
    for (BkgDocumentStatus terminalState : terminalStates) {
      BookingStateMachine bookingStateMachine = BookingStateMachine.startFromExistingState(terminalState);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::cancel);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::pendingUpdate);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::complete);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::pendingConfirmation);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::reject);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::complete);
    }
    BookingStateMachine bookingStateMachine = BookingStateMachine.startFromExistingState(PENU);
    Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::confirm);
    Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::complete);
  }

  @Test
  void verifyThatAllStatesAreReachable() {
    Set<BkgDocumentStatus> reachableStates = new HashSet<>(BookingStateMachineTables.BOOKING_STATE_TRANSITION_TABLE.size());
    Set<BkgDocumentStatus> pendingStates = new HashSet<>(BookingStateMachineTables.BOOKING_STATE_TRANSITION_TABLE.size());
    int loopCounter = 0;
    // The implementation should visit every state at most once.  If it does not, then someone
    // introduced a bug that (likely) makes it loop forever.
    final int loopMax = values().length;
    pendingStates.add(BookingStateMachineTables.INITIAL_STATE);

    while (!pendingStates.isEmpty()) {
      BkgDocumentStatus nextState = pendingStates.iterator().next();
      pendingStates.remove(nextState);
      reachableStates.add(nextState);
      Set<BkgDocumentStatus> newStates = BookingStateMachineTables.BOOKING_STATE_TRANSITION_TABLE.get(nextState);
      Assertions.assertNotNull(newStates, "The state " + nextState.name() + " was reachable, but it was not declared!");
      newStates.stream().filter(s -> !reachableStates.contains(s)).forEach(pendingStates::add);
      if (loopCounter++ > loopMax) {
        throw new IllegalStateException("Non-termination! Reachable " + reachableStates + " vs. pending " + pendingStates);
      }
    }
    Assertions.assertEquals(BookingStateMachineTables.BOOKING_STATE_TRANSITION_TABLE.keySet(), reachableStates);
  }
}
