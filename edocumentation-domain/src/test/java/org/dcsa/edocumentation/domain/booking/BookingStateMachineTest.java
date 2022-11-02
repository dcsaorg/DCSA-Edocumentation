package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.InternalServerErrorException;
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
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::cancel);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::pendingUpdate);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::complete);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::pendingConfirmation);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::reject);
      Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::complete);
    }
    BookingStateMachine bookingStateMachine = BookingStateMachine.resumeFromState(PENU);
    Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::confirm);
    Assertions.assertThrows(InternalServerErrorException.class, bookingStateMachine::complete);
  }
}
