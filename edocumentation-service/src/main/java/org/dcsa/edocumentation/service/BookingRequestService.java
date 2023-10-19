package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRequestRepository;
import org.dcsa.edocumentation.service.mapping.BookingRequestMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentGroupMapper;
import org.dcsa.edocumentation.transferobjects.BookingRequestRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingRequestService {
  private final VoyageService voyageService;
  private final VesselService vesselService;
  private final ReferenceService referenceService;
  private final DocumentPartyService documentPartyService;
  private final ShipmentLocationService shipmentLocationService;

  private final BookingRequestRepository bookingRequestRepository;

  private final BookingRequestMapper bookingRequestMapper;

  private final RequestedEquipmentGroupMapper requestedEquipmentGroupMapper;

  @Transactional
  public Optional<BookingRequestTO> getBookingRequest(String carrierBookingRequestReference) {
    return bookingRequestRepository
      .findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
      .map(bookingRequestMapper::toDTO);
  }

  @Transactional
  public BookingRequestRefStatusTO createBookingRequest(BookingRequestTO bookingRequest) {
    BookingRequest booking = mapToBooking(bookingRequest);

    booking.receive();
    booking = saveBookingRequest(booking, bookingRequest);

    return bookingRequestMapper.toStatusDTO(booking);
  }

  @Transactional
  public Optional<BookingRequestRefStatusTO> updateBookingRequest(String carrierBookingRequestReference, BookingRequestTO bookingRequest) {
    BookingRequest existingBookingRequest = bookingRequestRepository.findBookingByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (existingBookingRequest == null) {
      return Optional.empty();
    }
    OffsetDateTime updateTime = OffsetDateTime.now();
    existingBookingRequest.lockVersion(updateTime);

    var updatedBooking = mapToBooking(bookingRequest, existingBookingRequest);
    updatedBooking.pendingUpdatesConfirmation(null, updateTime);
    // We have to flush the existing booking. Otherwise, JPA might be tempted to re-order that
    // write/save until after the updatedBooking is saved (which triggers the unique constraint
    // in the database).
    bookingRequestRepository.saveAndFlush(existingBookingRequest);
    updatedBooking = saveBookingRequest(updatedBooking, bookingRequest);

    // A couple of fail-safe checks that should be unnecessary unless we introduce bugs.
    assert existingBookingRequest.getValidUntil() != null;
    assert Objects.equals(existingBookingRequest.getCarrierBookingRequestReference(), updatedBooking.getBookingChannelReference());
    assert updatedBooking.getId() != null && !updatedBooking.getId().equals(existingBookingRequest.getId());

    return Optional.of(bookingRequestMapper.toStatusDTO(updatedBooking));
  }

  private void createDeepObjectsForBookingRequest(BookingRequestTO bookingRequest, BookingRequest booking) {
    referenceService.createReferences(bookingRequest.references(), booking);
    documentPartyService.createDocumentParties(bookingRequest.documentParties(), booking);
    shipmentLocationService.createShipmentLocations(bookingRequest.shipmentLocations(), booking);
  }

  @Transactional
  public Optional<BookingRequestRefStatusTO> cancelBookingRequest(String carrierBookingRequestReference,
                                                                  String reason) {
    BookingRequest bookingRequest = bookingRequestRepository.findBookingByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (bookingRequest == null) {
      return Optional.empty();
    }
    OffsetDateTime updateTime = OffsetDateTime.now();
    // This works because we do not need to support versioning/rollback
    bookingRequest.cancel(reason, updateTime);
    bookingRequest = bookingRequestRepository.save(bookingRequest);
    return Optional.of(bookingRequestMapper.toStatusDTO(bookingRequest));
  }

  private BookingRequest saveBookingRequest(BookingRequest booking, BookingRequestTO bookingRequest) {
    BookingRequest updatedBookingRequest = bookingRequestRepository.save(booking);
    createDeepObjectsForBookingRequest(bookingRequest, updatedBookingRequest);
    return updatedBookingRequest;
  }

  private BookingRequest mapToBooking(BookingRequestTO bookingRequest) {
    return mapToBooking(bookingRequest, null);
  }

  private BookingRequest mapToBooking(BookingRequestTO bookingRequest, BookingRequest updateFor) {
    var voyage = voyageService.resolveVoyage(bookingRequest);
    var vessel = vesselService.resolveVessel(bookingRequest);
    var bookingBuilder = bookingRequestMapper.toDAO(bookingRequest, voyage, vessel).toBuilder();
    if (updateFor != null) {
      bookingBuilder = bookingBuilder
        // Carry over from the existing booking
        // FIXME: This should not be done via a builder.
        .carrierBookingRequestReference(updateFor.getCarrierBookingRequestReference())
        .bookingStatus(updateFor.getBookingStatus())
        .bookingRequestCreatedDateTime(updateFor.getBookingRequestCreatedDateTime());
    }
    var booking = bookingBuilder.build();
    booking.assignRequestedEquipment(
      bookingRequest.requestedEquipments()
        .stream()
        .map(requestedEquipmentGroupMapper::toDAO)
        .toList()
    );
    return booking;
  }
}
