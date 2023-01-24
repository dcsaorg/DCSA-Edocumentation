package org.dcsa.edocumentation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.decoupled.repository.BookingRepository;
import org.dcsa.edocumentation.domain.decoupled.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EventClassifierCode;
import org.dcsa.edocumentation.service.util.BookingStateMachine;
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

@Service
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
    OffsetDateTime now = OffsetDateTime.now();
    return saveBookingAndEvent(bookingRequest, BkgDocumentStatus.RECE, now, now, null);
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public BookingRefStatusTO updateBooking(String carrierBookingRequestReference, BookingTO bookingRequest) {
    return updateExistingBooking(carrierBookingRequestReference, existingBooking -> bookingRequest, BkgDocumentStatus.PENC, null);
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public BookingRefStatusTO cancelBooking(String carrierBookingRequestReference, String reason) {
    return updateBookingStatus(carrierBookingRequestReference, BkgDocumentStatus.CANC, reason);
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public BookingRefStatusTO updateBookingStatus(String carrierBookingRequestReference, BkgDocumentStatus documentStatus, String reason) {
    return updateExistingBooking(carrierBookingRequestReference, this::deserializeTOFromBooking, documentStatus, reason);
  }

  private BookingRefStatusTO updateExistingBooking(String carrierBookingRequestReference, Function<Booking, BookingTO> bookingTOSupplier, BkgDocumentStatus documentStatus, String reason) {
    Booking existingBooking = getBookingOrThrow(carrierBookingRequestReference);
    BookingStateMachine.validateTransition(existingBooking.getDocumentStatus(), bookingMapper.toDAO(documentStatus));

    OffsetDateTime now = OffsetDateTime.now();
    existingBooking.lockVersion(now);
    bookingRepository.saveAndFlush(existingBooking);

    return saveBookingAndEvent(bookingTOSupplier.apply(existingBooking), documentStatus, existingBooking.getBookingRequestCreatedDateTime(), now, reason);
  }

  private BookingRefStatusTO saveBookingAndEvent(BookingTO bookingTO, BkgDocumentStatus documentStatus, OffsetDateTime createTime, OffsetDateTime updateTime, String reason) {
    BookingTO updatedRequest = updateRequest(bookingTO, documentStatus, createTime, updateTime);
    Booking booking = bookingRepository.save(bookingFromTO(updatedRequest));
    shipmentEventRepository.save(shipmentEventFromBooking(booking, reason));
    return bookingMapper.toStatusDTO(updatedRequest);
  }

  private Booking getBookingOrThrow(String carrierBookingRequestReference) {
    return bookingRepository.findByCarrierBookingRequestReference(carrierBookingRequestReference)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No booking found with carrierBookingRequestReference: " + carrierBookingRequestReference));
  }

  private BookingTO updateRequest(BookingTO bookingRequest, BkgDocumentStatus documentStatus, OffsetDateTime createTime, OffsetDateTime updateTime) {
    String carrierBookingRequestReference =
      Objects.requireNonNullElseGet(bookingRequest.carrierBookingRequestReference(), () -> UUID.randomUUID().toString());

    return bookingRequest.toBuilder()
      .carrierBookingRequestReference(carrierBookingRequestReference)
      .bookingRequestCreatedDateTime(createTime)
      .bookingRequestUpdatedDateTime(updateTime)
      .documentStatus(documentStatus)
      .build();
  }

  @SneakyThrows
  private Booking bookingFromTO(BookingTO bookingTO) {
    return Booking.builder()
      .carrierBookingRequestReference(bookingTO.carrierBookingRequestReference())
      .documentStatus(bookingMapper.toDAO(bookingTO.documentStatus()))
      .content(objectMapper.writeValueAsString(bookingTO))
      .bookingRequestCreatedDateTime(bookingTO.bookingRequestCreatedDateTime())
      .bookingRequestUpdatedDateTime(bookingTO.bookingRequestUpdatedDateTime())
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
