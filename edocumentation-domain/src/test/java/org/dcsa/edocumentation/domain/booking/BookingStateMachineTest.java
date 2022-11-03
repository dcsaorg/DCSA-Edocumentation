package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;


class BookingStateMachineTest {

  @Test
  void testBasic() {
    BookingStateMachine bookingStateMachine = BookingStateMachine.fromInitialState();
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
      BookingStateMachine bookingStateMachine = BookingStateMachine.resumeFromState(terminalState);
      Assertions.assertThrows(ConflictException.class, bookingStateMachine::cancel);
      Assertions.assertThrows(ConflictException.class, bookingStateMachine::pendingUpdate);
      Assertions.assertThrows(ConflictException.class, bookingStateMachine::complete);
      Assertions.assertThrows(ConflictException.class, bookingStateMachine::pendingConfirmation);
      Assertions.assertThrows(ConflictException.class, bookingStateMachine::reject);
      Assertions.assertThrows(ConflictException.class, bookingStateMachine::complete);
    }
    BookingStateMachine bookingStateMachine = BookingStateMachine.resumeFromState(PENU);
    Assertions.assertThrows(ConflictException.class, bookingStateMachine::confirm);
    Assertions.assertThrows(ConflictException.class, bookingStateMachine::complete);
  }
}
