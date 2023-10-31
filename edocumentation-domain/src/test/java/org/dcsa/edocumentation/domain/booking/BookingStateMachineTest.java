package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.skernel.errors.exceptions.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingStateMachineTest {

  @Test
  void testBasic() {
    var booking = Booking.builder().build();
    var bookingData = BookingData.builder().build();
    booking.receive();
    Assertions.assertEquals(BookingStatus.RECEIVED, booking.getBookingStatus());
    booking.pendingUpdatesConfirmation(bookingData);
    Assertions.assertEquals(BookingStatus.PENDING_UPDATES_CONFIRMATION, booking.getBookingStatus());
    booking.pendingUpdate("Please provide foo!");
    Assertions.assertEquals(BookingStatus.PENDING_UPDATE, booking.getBookingStatus());

    booking = Booking.builder().bookingStatus(BookingStatus.RECEIVED).build();
    Assertions.assertEquals(BookingStatus.RECEIVED, booking.getBookingStatus());
    booking.pendingUpdatesConfirmation(bookingData);
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
    var bookingData = BookingData.builder().build();
    for (String terminalState : terminalStates) {
      var booking = Booking.builder().bookingStatus(terminalState).build();
      Assertions.assertThrows(ConflictException.class,
        () -> booking.cancel("We decided to booking somewhere else."));
      Assertions.assertThrows(ConflictException.class, () -> booking.pendingUpdate("Please provide foo!"));
      Assertions.assertThrows(ConflictException.class, booking::complete);
      Assertions.assertThrows(ConflictException.class,
        () -> booking.pendingUpdatesConfirmation(bookingData));
      Assertions.assertThrows(ConflictException.class, () -> booking.reject("We cannot provide the service."));
      Assertions.assertThrows(ConflictException.class, booking::complete);
    }
    var booking = Booking.builder().bookingStatus(BookingStatus.PENDING_UPDATE).build();
    Assertions.assertThrows(ConflictException.class, booking::confirm);
    Assertions.assertThrows(ConflictException.class, booking::complete);
  }
}
