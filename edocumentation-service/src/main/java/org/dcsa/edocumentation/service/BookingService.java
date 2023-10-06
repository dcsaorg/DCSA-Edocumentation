package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentGroupMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final VoyageService voyageService;
  private final VesselService vesselService;
  private final ReferenceService referenceService;
  private final DocumentPartyService documentPartyService;
  private final ShipmentLocationService shipmentLocationService;

  private final BookingRepository bookingRepository;

  private final BookingMapper bookingMapper;

  private final RequestedEquipmentGroupMapper requestedEquipmentGroupMapper;

  @Transactional
  public Optional<BookingTO> getBooking(String carrierBookingRequestReference) {
    return bookingRepository
      .findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
      .map(bookingMapper::toDTO);
  }

  @Transactional
  public BookingRefStatusTO createBooking(BookingTO bookingRequest) {
    Booking booking = mapToBooking(bookingRequest);

    booking.receive();
    booking = saveBooking(booking, bookingRequest);

    return bookingMapper.toStatusDTO(booking);
  }

  @Transactional
  public Optional<BookingRefStatusTO> updateBooking(String carrierBookingRequestReference, BookingTO bookingRequest) {
    Booking existingBooking = bookingRepository.findBookingByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (existingBooking == null) {
      return Optional.empty();
    }
    OffsetDateTime updateTime = OffsetDateTime.now();
    existingBooking.lockVersion(updateTime);

    var updatedBooking = mapToBooking(bookingRequest, existingBooking);
    updatedBooking.pendingConfirmation(null, updateTime);
    // We have to flush the existing booking. Otherwise, JPA might be tempted to re-order that
    // write/save until after the updatedBooking is saved (which triggers the unique constraint
    // in the database).
    bookingRepository.saveAndFlush(existingBooking);
    updatedBooking = saveBooking(updatedBooking, bookingRequest);

    // A couple of fail-safe checks that should be unnecessary unless we introduce bugs.
    assert existingBooking.getValidUntil() != null;
    assert Objects.equals(existingBooking.getCarrierBookingRequestReference(), updatedBooking.getBookingChannelReference());
    assert updatedBooking.getId() != null && !updatedBooking.getId().equals(existingBooking.getId());

    return Optional.of(bookingMapper.toStatusDTO(updatedBooking));
  }

  private void createDeepObjectsForBooking(BookingTO bookingRequest, Booking booking) {
    referenceService.createReferences(bookingRequest.references(), booking);
    documentPartyService.createDocumentParties(bookingRequest.documentParties(), booking);
    shipmentLocationService.createShipmentLocations(bookingRequest.shipmentLocations(), booking);
  }

  @Transactional
  public Optional<BookingRefStatusTO> cancelBooking(String carrierBookingRequestReference,
                                                    String reason) {
    Booking booking = bookingRepository.findBookingByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (booking == null) {
      return Optional.empty();
    }
    OffsetDateTime updateTime = OffsetDateTime.now();
    // This works because we do not need to support versioning/rollback
    booking.cancel(reason, updateTime);
    booking = bookingRepository.save(booking);
    return Optional.of(bookingMapper.toStatusDTO(booking));
  }

  private Booking saveBooking(Booking booking, BookingTO bookingRequest) {
    Booking updatedBooking = bookingRepository.save(booking);
    createDeepObjectsForBooking(bookingRequest, updatedBooking);
    return updatedBooking;
  }

  private Booking mapToBooking(BookingTO bookingRequest) {
    return mapToBooking(bookingRequest, null);
  }

  private Booking mapToBooking(BookingTO bookingRequest, Booking updateFor) {
    var voyage = voyageService.resolveVoyage(bookingRequest);
    var vessel = vesselService.resolveVessel(bookingRequest);
    var bookingBuilder = bookingMapper.toDAO(bookingRequest, voyage, vessel).toBuilder();
    if (updateFor != null) {
      bookingBuilder = bookingBuilder
        // Carry over from the existing booking
        // FIXME: This should not be done via a builder.
        .carrierBookingRequestReference(updateFor.getCarrierBookingRequestReference())
        .documentStatus(updateFor.getDocumentStatus())
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
