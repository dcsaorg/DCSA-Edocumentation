package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

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

  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;

  public Optional<BookingTO> getBooking(String carrierBookingRequestReference) {
    return bookingRepository
      .findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
      .map(bookingMapper::toDTO);
  }

  @Transactional
  public BookingRefStatusTO createBooking(BookingTO bookingRequest) {
    OffsetDateTime now = OffsetDateTime.now();
    Booking.BookingBuilder bookingToSave = bookingMapper.toDAO(bookingRequest).toBuilder();

    Booking booking = bookingRepository.save(
      bookingToSave
        .carrierBookingRequestReference(UUID.randomUUID().toString())
        .documentStatus(BkgDocumentStatus.RECE)
        .bookingRequestCreatedDateTime(now)
        .bookingRequestUpdatedDateTime(now)
        .voyage(voyageService.resolveVoyage(bookingRequest))
        .vessel(vesselService.resolveVessel(bookingRequest))
        .placeOfIssue(locationService.ensureResolvable(bookingRequest.placeOfBLIssue()))
        .invoicePayableAt(locationService.ensureResolvable(bookingRequest.invoicePayableAt()))
        .build()
    );

    commodityService.createCommodities(bookingRequest.commodities(), booking);
    valueAddedServiceRequestService.createValueAddedServiceRequests(bookingRequest.valueAddedServiceRequests(), booking);
    referenceService.createReferences(bookingRequest.references(), booking);
    requestedEquipmentService.createRequestedEquipments(bookingRequest.requestedEquipments(), booking);
    documentPartyService.createDocumentParties(bookingRequest.documentParties(), booking);
    shipmentLocationService.createShipmentLocations(bookingRequest.shipmentLocations(), booking);

    return bookingMapper.toStatusDTO(booking);
  }

  @Transactional
  public BookingRefStatusTO updateBooking(String carrierBookingRequestReference, BookingTO bookingRequest) {
    return null; // TODO https://dcsa.atlassian.net/browse/DDT-1277
  }
}
