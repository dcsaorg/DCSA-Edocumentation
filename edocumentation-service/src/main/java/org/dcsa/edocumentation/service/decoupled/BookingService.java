package org.dcsa.edocumentation.service.decoupled;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.decoupled.repository.BookingRepository;
import org.dcsa.edocumentation.domain.decoupled.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EventClassifierCode;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service("decoupledBookingService")
@RequiredArgsConstructor
public class BookingService {
  private final BookingRepository bookingRepository;
  private final ShipmentEventRepository shipmentEventRepository;
  private final BookingMapper bookingMapper;
  private final ObjectMapper objectMapper;

  @Transactional(transactionManager = "decoupledTransactionManager")
  public BookingTO getBooking(String carrierBookingRequestReference) {
    return deserializeTOFromBooking(getBookingOrThrow(carrierBookingRequestReference));
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public BookingRefStatusTO createBooking(BookingTO bookingRequest) {
    return saveBookingAndEvent(bookingRequest, BkgDocumentStatus.RECE, OffsetDateTime.now(), null);
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public BookingRefStatusTO updateBooking(String carrierBookingRequestReference, BookingTO bookingRequest) {
    return updateExistingBooking(carrierBookingRequestReference, existingBooking -> bookingRequest, BkgDocumentStatus.PENC, null);
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public BookingRefStatusTO cancelBooking(String carrierBookingRequestReference, String reason) {
    return updateExistingBooking(carrierBookingRequestReference, this::deserializeTOFromBooking, BkgDocumentStatus.CANC, reason);
  }

  private BookingRefStatusTO updateExistingBooking(String carrierBookingRequestReference, Function<Booking, BookingTO> bookingTOSupplier, BkgDocumentStatus documentStatus, String reason) {
    Booking existingBooking = getBookingOrThrow(carrierBookingRequestReference);
    BookingStateMachine.validateTransition(existingBooking.getDocumentStatus(), bookingMapper.toDAO(documentStatus));

    OffsetDateTime now = OffsetDateTime.now();
    existingBooking.lockVersion(now);
    bookingRepository.saveAndFlush(existingBooking);

    return saveBookingAndEvent(bookingTOSupplier.apply(existingBooking), documentStatus, now, reason);
  }

  private BookingRefStatusTO saveBookingAndEvent(BookingTO bookingTO, BkgDocumentStatus documentStatus, OffsetDateTime time, String reason) {
    BookingTO updatedRequest = updateRequest(bookingTO, documentStatus, time);
    Booking booking = bookingRepository.save(bookingFromTO(updatedRequest));
    shipmentEventRepository.save(shipmentEventFromBooking(booking, reason));
    return bookingMapper.toStatusDTO(updatedRequest);
  }

  private Booking getBookingOrThrow(String carrierBookingRequestReference) {
    return bookingRepository.findByCarrierBookingRequestReference(carrierBookingRequestReference)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No booking found with carrierBookingRequestReference: " + carrierBookingRequestReference));
  }

  private BookingTO updateRequest(BookingTO bookingRequest, BkgDocumentStatus documentStatus, OffsetDateTime time) {
    OffsetDateTime created =
      Objects.requireNonNullElse(bookingRequest.bookingRequestCreatedDateTime(), time);
    String carrierBookingRequestReference =
      Objects.requireNonNullElseGet(bookingRequest.carrierBookingRequestReference(), () -> UUID.randomUUID().toString());

    return bookingRequest.toBuilder()
      .carrierBookingRequestReference(carrierBookingRequestReference)
      .bookingRequestCreatedDateTime(created)
      .bookingRequestUpdatedDateTime(time)
      .documentStatus(documentStatus)
      .build();
  }

  @SneakyThrows
  private Booking bookingFromTO(BookingTO bookingRequest) {
    return Booking.builder()
      .carrierBookingRequestReference(bookingRequest.carrierBookingRequestReference())
      .documentStatus(bookingMapper.toDAO(bookingRequest.documentStatus()))
      .content(objectMapper.writeValueAsString(bookingRequest))
      .bookingRequestCreatedDateTime(bookingRequest.bookingRequestCreatedDateTime())
      .bookingRequestUpdatedDateTime(bookingRequest.bookingRequestUpdatedDateTime())
      .build();
  }

  @SneakyThrows
  private BookingTO deserializeTOFromBooking(Booking booking) {
    return objectMapper.readValue(booking.getContent(), BookingTO.class);
  }

  private ShipmentEvent shipmentEventFromBooking(Booking booking, String reason) {
    return ShipmentEvent.builder()
      .documentID(booking.getId())
      .documentReference(booking.getCarrierBookingRequestReference())
      .documentTypeCode(DocumentTypeCode.CBR)
      .shipmentEventTypeCode(booking.getDocumentStatus().asShipmentEventTypeCode())
      .reason(reason)
      .eventClassifierCode(EventClassifierCode.ACT)
      .eventDateTime(booking.getBookingRequestUpdatedDateTime())
      .eventCreatedDateTime(booking.getBookingRequestUpdatedDateTime())
      .build();
  }
}
