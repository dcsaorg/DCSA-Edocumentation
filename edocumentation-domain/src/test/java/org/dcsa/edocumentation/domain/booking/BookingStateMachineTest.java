package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;


class BookingStateMachineTest {

  @Test
  void testBasic() {
    Booking booking = Booking.builder().build();
    OffsetDateTime now = OffsetDateTime.now();
    booking.receive();
    Assertions.assertEquals(RECE, booking.getDocumentStatus());
    booking.pendingConfirmation("We provided what you requested. Please review it and confirm the booking", now);
    Assertions.assertEquals(PENC, booking.getDocumentStatus());
    booking.pendingUpdate("Please provide foo!", now);
    Assertions.assertEquals(PENU, booking.getDocumentStatus());

    booking = Booking.builder().documentStatus(RECE).build();
    Assertions.assertEquals(RECE, booking.getDocumentStatus());
    booking.pendingConfirmation("We provided what you requested. Please review it and confirm the booking", now);
    Assertions.assertEquals(PENC, booking.getDocumentStatus());
  }

  @Test
  void loadInvalidStateTransitions() {
    BkgDocumentStatus[] terminalStates = {
      CANC,
      REJE,
      CMPL
    };
    OffsetDateTime now = OffsetDateTime.now();
    for (BkgDocumentStatus terminalState : terminalStates) {
      Booking booking = Booking.builder().documentStatus(terminalState).build();
      Assertions.assertThrows(ConflictException.class,
        () -> booking.cancel("We decided to booking somewhere else.", now));
      Assertions.assertThrows(ConflictException.class, () -> booking.pendingUpdate("Please provide foo!", now));
      Assertions.assertThrows(ConflictException.class, booking::complete);
      Assertions.assertThrows(ConflictException.class,
        () -> booking.pendingConfirmation(
          "We provided what you requested. Please review it and confirm the booking",
          now
      ));
      Assertions.assertThrows(ConflictException.class, () -> booking.reject("We cannot provide the service."));
      Assertions.assertThrows(ConflictException.class, booking::complete);
    }
    Booking booking = Booking.builder().documentStatus(PENU).build();
    Assertions.assertThrows(ConflictException.class, booking::confirm);
    Assertions.assertThrows(ConflictException.class, booking::complete);
  }
}
