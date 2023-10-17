package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.skernel.errors.exceptions.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;


class BookingStateMachineTest {

  @Test
  void testBasic() {
    Booking booking = Booking.builder().build();
    OffsetDateTime now = OffsetDateTime.now();
    booking.receive();
    Assertions.assertEquals(BookingStatus.RECEIVED, booking.getBookingStatus());
    booking.pendingUpdatesConfirmation("We provided what you requested. Please review it and confirm the booking", now);
    Assertions.assertEquals(BookingStatus.PENDING_UPDATES_CONFIRMATION, booking.getBookingStatus());
    booking.pendingUpdate("Please provide foo!", now);
    Assertions.assertEquals(BookingStatus.PENDING_UPDATE, booking.getBookingStatus());

    booking = Booking.builder().bookingStatus(BookingStatus.RECEIVED).build();
    Assertions.assertEquals(BookingStatus.RECEIVED, booking.getBookingStatus());
    booking.pendingUpdatesConfirmation("We provided what you requested. Please review it and confirm the booking", now);
    Assertions.assertEquals(BookingStatus.PENDING_UPDATES_CONFIRMATION, booking.getBookingStatus());
  }

  @Test
  void loadInvalidStateTransitions() {
    String[] terminalStates = {
      BookingStatus.COMPLETED,
      BookingStatus.REJECTED,
      BookingStatus.DECLINED,
      BookingStatus.CANCELLED
    };
    OffsetDateTime now = OffsetDateTime.now();
    for (String terminalState : terminalStates) {
      Booking booking = Booking.builder().bookingStatus(terminalState).build();
      Assertions.assertThrows(ConflictException.class,
        () -> booking.cancel("We decided to booking somewhere else.", now));
      Assertions.assertThrows(ConflictException.class, () -> booking.pendingUpdate("Please provide foo!", now));
      Assertions.assertThrows(ConflictException.class, booking::complete);
      Assertions.assertThrows(ConflictException.class,
        () -> booking.pendingUpdatesConfirmation(
          "We provided what you requested. Please review it and confirm the booking",
          now
      ));
      Assertions.assertThrows(ConflictException.class, () -> booking.reject("We cannot provide the service."));
      Assertions.assertThrows(ConflictException.class, booking::complete);
    }
    Booking booking = Booking.builder().bookingStatus(BookingStatus.PENDING_UPDATE).build();
    Assertions.assertThrows(ConflictException.class, () -> booking.confirm(now));
    Assertions.assertThrows(ConflictException.class, booking::complete);
  }
}
