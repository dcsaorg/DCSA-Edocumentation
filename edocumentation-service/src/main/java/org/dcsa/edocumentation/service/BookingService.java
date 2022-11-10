package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.transferobjects.BookingCancelRequestTO;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

  private final BookingRepository bookingRepository;
  private final ShipmentEventRepository shipmentEventRepository;

  private final BookingMapper bookingMapper;
  private final DocumentStatusMapper documentStatusMapper;

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
      .placeOfIssue(locationService.ensureResolvable(bookingRequest.placeOfBLIssue()))
      .invoicePayableAt(locationService.ensureResolvable(bookingRequest.invoicePayableAt()))
      .build();

    shipmentEventRepository.save(booking.receive());
    booking = bookingRepository.save(booking);

    createDeepObjectsForBooking(bookingRequest, booking);

    return bookingMapper.toStatusDTO(booking);
  }

  @Transactional
  public BookingRefStatusTO updateBooking(String carrierBookingRequestReference, BookingTO bookingRequest) {
    return null; // TODO https://dcsa.atlassian.net/browse/DDT-1277
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
