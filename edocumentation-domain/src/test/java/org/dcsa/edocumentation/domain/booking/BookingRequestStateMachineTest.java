package org.dcsa.edocumentation.domain.booking;

import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.skernel.errors.exceptions.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;


class BookingRequestStateMachineTest {

  @Test
  void testBasic() {
    BookingRequest bookingRequest = BookingRequest.builder().build();
    OffsetDateTime now = OffsetDateTime.now();
    bookingRequest.receive();
    Assertions.assertEquals(BookingStatus.RECEIVED, bookingRequest.getBookingStatus());
    bookingRequest.pendingUpdatesConfirmation("We provided what you requested. Please review it and confirm the booking", now);
    Assertions.assertEquals(BookingStatus.PENDING_UPDATES_CONFIRMATION, bookingRequest.getBookingStatus());
    bookingRequest.pendingUpdate("Please provide foo!", now);
    Assertions.assertEquals(BookingStatus.PENDING_UPDATE, bookingRequest.getBookingStatus());

    bookingRequest = BookingRequest.builder().bookingStatus(BookingStatus.RECEIVED).build();
    Assertions.assertEquals(BookingStatus.RECEIVED, bookingRequest.getBookingStatus());
    bookingRequest.pendingUpdatesConfirmation("We provided what you requested. Please review it and confirm the booking", now);
    Assertions.assertEquals(BookingStatus.PENDING_UPDATES_CONFIRMATION, bookingRequest.getBookingStatus());
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
      BookingRequest bookingRequest = BookingRequest.builder().bookingStatus(terminalState).build();
      Assertions.assertThrows(ConflictException.class,
        () -> bookingRequest.cancel("We decided to booking somewhere else.", now));
      Assertions.assertThrows(ConflictException.class, () -> bookingRequest.pendingUpdate("Please provide foo!", now));
      Assertions.assertThrows(ConflictException.class, bookingRequest::complete);
      Assertions.assertThrows(ConflictException.class,
        () -> bookingRequest.pendingUpdatesConfirmation(
          "We provided what you requested. Please review it and confirm the booking",
          now
      ));
      Assertions.assertThrows(ConflictException.class, () -> bookingRequest.reject("We cannot provide the service."));
      Assertions.assertThrows(ConflictException.class, bookingRequest::complete);
    }
    BookingRequest bookingRequest = BookingRequest.builder().bookingStatus(BookingStatus.PENDING_UPDATE).build();
    Assertions.assertThrows(ConflictException.class, () -> bookingRequest.confirm(now));
    Assertions.assertThrows(ConflictException.class, bookingRequest::complete);
  }
}
