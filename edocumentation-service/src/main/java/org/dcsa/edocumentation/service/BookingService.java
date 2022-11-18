package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.service.mapping.ModeOfTransportMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final LocationService locationService;
  private final VoyageService voyageService;
  private final VesselService vesselService;
  private final CommodityService commodityService;
  private final ValueAddedServiceRequestService valueAddedServiceRequestService;
  private final RequestedEquipmentService requestedEquipmentService;
  private final ReferenceService referenceService;
  private final DocumentPartyService documentPartyService;
  private final ShipmentLocationService shipmentLocationService;
  private final ModeOfTransportService modeOfTransportService;

  private final BookingRepository bookingRepository;
  private final ShipmentEventRepository shipmentEventRepository;

  private final BookingMapper bookingMapper;
  private final ModeOfTransportMapper modeOfTransportMapper;

  public Optional<BookingTO> getBooking(String carrierBookingRequestReference) {
    return bookingRepository
      .findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
      .map(bookingMapper::toDTO);
  }

  @Transactional
  public BookingRefStatusTO createBooking(BookingTO bookingRequest) {
    Booking booking = bookingMapper.toDAO(bookingRequest).toBuilder()
      .voyage(voyageService.resolveVoyage(bookingRequest))
      .vessel(vesselService.resolveVessel(bookingRequest))
      .modeOfTransport(modeOfTransportService.resolveModeOfTransport(
        modeOfTransportMapper.toDAO(bookingRequest.preCarriageModeOfTransportCode())))
      .placeOfIssue(locationService.ensureResolvable(bookingRequest.placeOfBLIssue()))
      .invoicePayableAt(locationService.ensureResolvable(bookingRequest.invoicePayableAt()))
      .build();

    shipmentEventRepository.save(booking.receive());
    booking = bookingRepository.save(booking);

    createDeepObjectsForBooking(bookingRequest, booking);

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
    Booking updatedBooking = bookingMapper.toDAO(bookingRequest).toBuilder()
      .voyage(voyageService.resolveVoyage(bookingRequest))
      .vessel(vesselService.resolveVessel(bookingRequest))
      .placeOfIssue(locationService.ensureResolvable(bookingRequest.placeOfBLIssue()))
      .invoicePayableAt(locationService.ensureResolvable(bookingRequest.invoicePayableAt()))
      // Carry over from the existing booking
      .carrierBookingRequestReference(existingBooking.getCarrierBookingRequestReference())
      .documentStatus(existingBooking.getDocumentStatus())
      .bookingRequestCreatedDateTime(existingBooking.getBookingRequestCreatedDateTime())
      .build();

    // TODO: If we get "soft validation", we would have to conditionally call pendingUpdate here
    ShipmentEvent updateEvent = updatedBooking.pendingConfirmation(null, updateTime);
    // We have to flush the existing booking. Otherwise, JPA might be tempted to re-order that
    // write/save until after the updatedBooking is saved (which triggers the unique constraint
    // in the database).
    bookingRepository.saveAndFlush(existingBooking);
    updatedBooking = bookingRepository.save(updatedBooking);
    shipmentEventRepository.save(updateEvent);

    // A couple of fail-safe checks that should be unnecessary unless we introduce bugs.
    assert existingBooking.getValidUntil() != null;
    assert Objects.equals(existingBooking.getCarrierBookingRequestReference(), updatedBooking.getBookingChannelReference());
    assert updatedBooking.getId() != null && !updatedBooking.getId().equals(existingBooking.getId());

    createDeepObjectsForBooking(bookingRequest, updatedBooking);
    return Optional.of(bookingMapper.toStatusDTO(updatedBooking));
  }

  private void createDeepObjectsForBooking(BookingTO bookingRequest, Booking booking) {
    commodityService.createCommodities(bookingRequest.commodities(), booking);
    valueAddedServiceRequestService.createValueAddedServiceRequests(bookingRequest.valueAddedServiceRequests(), booking);
    referenceService.createReferences(bookingRequest.references(), booking);
    requestedEquipmentService.createRequestedEquipments(bookingRequest.requestedEquipments(), booking);
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
    ShipmentEvent event = booking.cancel(reason);
    bookingRepository.save(booking);
    shipmentEventRepository.save(event);
    return Optional.of(bookingMapper.toStatusDTO(booking));
  }
}
