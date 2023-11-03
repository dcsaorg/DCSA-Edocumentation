package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingDataMapper;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
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

  private final BookingRepository bookingRepository;

  private final BookingMapper bookingMapper;
  private final BookingDataMapper bookingDataMapper;

  private final RequestedEquipmentGroupMapper requestedEquipmentGroupMapper;

  @Transactional
  public Optional<BookingRequestTO> getBookingRequest(String carrierBookingRequestReference) {
    return bookingRepository
      .findByCarrierBookingRequestReference(carrierBookingRequestReference)
      .map(bookingMapper::toDTO);
  }

  @Transactional
  public BookingRequestRefStatusTO createBookingRequest(BookingRequestTO bookingRequest) {
    var bookingData = mapToBookingData(bookingRequest);
    var booking = Booking.builder()
      .bookingData(bookingData)
      .build();

    booking.receive();
    booking = saveBooking(booking, bookingRequest);

    return bookingMapper.toStatusDTO(booking);
  }

  @Transactional
  public Optional<BookingRequestRefStatusTO> updateBookingRequest(String carrierBookingRequestReference, BookingRequestTO bookingRequest) {
    Booking existingBooking = bookingRepository.findByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (existingBooking == null) {
      return Optional.empty();
    }

    var updatedBookingData = mapToBookingData(bookingRequest);
    existingBooking.pendingUpdatesConfirmation(updatedBookingData);
    // We have to flush the existing booking. Otherwise, JPA might be tempted to re-order that
    // write/save until after the updatedBooking is saved (which triggers the unique constraint
    // in the database).
    existingBooking = bookingRepository.saveAndFlush(existingBooking);
    existingBooking = saveBooking(existingBooking, bookingRequest);

    // A couple of fail-safe checks that should be unnecessary unless we introduce bugs.
    assert Objects.equals(existingBooking.getCarrierBookingRequestReference(), updatedBookingData.getBookingChannelReference());
    assert updatedBookingData.getId() != null && !updatedBookingData.getId().equals(existingBooking.getId());

    return Optional.of(bookingMapper.toStatusDTO(existingBooking));
  }

  private void createDeepObjectsForBookingData(BookingRequestTO bookingRequest, BookingData bookingData) {
    referenceService.createReferences(bookingRequest.references(), bookingData);
    documentPartyService.createDocumentParties(bookingRequest.documentParties(), bookingData);
    shipmentLocationService.createShipmentLocations(bookingRequest.shipmentLocations(), bookingData);
  }

  @Transactional
  public Optional<BookingRequestRefStatusTO> cancelBooking(String carrierBookingRequestReference,
                                                           String reason) {
    var booking = bookingRepository.findByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (booking == null) {
      return Optional.empty();
    }
    // This works because we do not need to support versioning/rollback
    booking.cancel(reason);
    booking = bookingRepository.save(booking);
    return Optional.of(bookingMapper.toStatusDTO(booking));
  }

  private Booking saveBooking(Booking booking, BookingRequestTO bookingRequest) {
    var updatedBooking = bookingRepository.save(booking);
    createDeepObjectsForBookingData(bookingRequest, updatedBooking.getBookingData());
    return updatedBooking;
  }

  private BookingData mapToBookingData(BookingRequestTO bookingRequest) {
    var voyage = voyageService.resolveVoyage(bookingRequest);
    var vessel = vesselService.resolveVessel(bookingRequest);
    var booking = bookingDataMapper.toDAO(bookingRequest, voyage, vessel);
    booking.assignRequestedEquipment(
      bookingRequest.requestedEquipments()
        .stream()
        .map(requestedEquipmentGroupMapper::toDAO)
        .toList()
    );
    return booking;
  }
}
