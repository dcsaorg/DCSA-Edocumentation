package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;


class BookingStateMachineTest {

  @Test
  void testBasic() {
    Booking booking = Booking.builder().build();
    Assertions.assertEquals(RECE, booking.getDocumentStatus());
    booking.pendingConfirmation();
    Assertions.assertEquals(PENC, booking.getDocumentStatus());
    booking.pendingUpdate();
    Assertions.assertEquals(PENU, booking.getDocumentStatus());
  }

  @Test
  void loadInvalidStateTransitions() {
    BkgDocumentStatus[] terminalStates = {
      CANC,
      REJE,
      CMPL
    };
    for (BkgDocumentStatus terminalState : terminalStates) {
      Booking booking = Booking.builder().documentStatus(terminalState).build();
      Assertions.assertThrows(ConflictException.class, booking::cancel);
      Assertions.assertThrows(ConflictException.class, booking::pendingUpdate);
      Assertions.assertThrows(ConflictException.class, booking::complete);
      Assertions.assertThrows(ConflictException.class, booking::pendingConfirmation);
      Assertions.assertThrows(ConflictException.class, booking::reject);
      Assertions.assertThrows(ConflictException.class, booking::complete);
    }
    Booking booking = Booking.builder().documentStatus(PENU).build();
    Assertions.assertThrows(ConflictException.class, booking::confirm);
    Assertions.assertThrows(ConflictException.class, booking::complete);
  }
}
