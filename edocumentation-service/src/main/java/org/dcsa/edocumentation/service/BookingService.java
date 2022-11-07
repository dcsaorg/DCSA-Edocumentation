package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentLocationRepository;
import org.dcsa.edocumentation.domain.persistence.repository.VesselRepository;
import org.dcsa.edocumentation.domain.persistence.repository.VoyageRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.service.mapping.CommodityMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentLocationMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.ValueAddedServiceRequestTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final BookingRepository bookingRepository;
  private final VoyageRepository voyageRepository;
  private final VesselRepository vesselRepository;
  private final LocationService locationService;
  private final CommodityRepository commodityRepository;
  private final ShipmentLocationRepository shipmentLocationRepository;

  private final BookingMapper bookingMapper;
  private final CommodityMapper commodityMapper;
  private final ShipmentLocationMapper shipmentLocationMapper;

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
        .voyage(resolveVoyage(bookingRequest))
        .vessel(resolveVessel(bookingRequest))
        .placeOfIssue(locationService.ensureResolvable(bookingRequest.placeOfBLIssue()))
        .invoicePayableAt(locationService.ensureResolvable(bookingRequest.invoicePayableAt()))
        .build()
    );

    createBookingRelatedEntities(bookingRequest, booking);

    return bookingMapper.toStatusDTO(booking);
  }

  @Transactional
  public BookingRefStatusTO updateBooking(String carrierBookingRequestReference, BookingTO bookingRequest) {
    return null; // TODO https://dcsa.atlassian.net/browse/DDT-1277
  }

  private Voyage resolveVoyage(BookingTO bookingRequest) {
    if (bookingRequest.carrierExportVoyageNumber() != null) {
      // Since carrierVoyageNumber is not unique in Voyage and booking does not supply a service to make it
      // unique we just take the first Voyage found.
      return voyageRepository.findByCarrierVoyageNumber(bookingRequest.carrierExportVoyageNumber()).stream()
        .findFirst()
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No voyages with carrierVoyageNumber = '" + bookingRequest.carrierExportVoyageNumber() + "'"));
    }
    return null;
  }

  private Vessel resolveVessel(BookingTO bookingRequest) {
    if (bookingRequest.vesselIMONumber() != null) {
      return vesselRepository.findByVesselIMONumber(bookingRequest.vesselIMONumber())
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No vessel with vesselIMONumber = '" + bookingRequest.vesselIMONumber() + "'"));
    }
    return null;
  }

  private void createBookingRelatedEntities(BookingTO bookingRequest, Booking booking) {
    List<CommodityTO> commodities = bookingRequest.commodities();
    if (commodities != null && !commodities.isEmpty()) {
      commodityRepository.saveAll(
        commodities.stream()
          .map(commodityTO -> commodityMapper.toDAO(commodityTO, booking))
          .toList()
      );
    }

    List<ValueAddedServiceRequestTO> valueAddedServiceRequests = bookingRequest.valueAddedServiceRequests();
    if (valueAddedServiceRequests != null && !valueAddedServiceRequests.isEmpty()) {

    }

    List<ReferenceTO> references = bookingRequest.references();
    if (references != null && !references.isEmpty()) {

    }

    List<RequestedEquipmentTO> requestedEquipments = bookingRequest.requestedEquipments();
    if (requestedEquipments != null && !requestedEquipments.isEmpty()) {
      // More stuff here
    }

    List<DocumentPartyTO> documentParties = bookingRequest.documentParties();
    if (documentParties != null && !documentParties.isEmpty()) {

    }

    List<ShipmentLocationTO> shipmentLocations = bookingRequest.shipmentLocations();
    if (shipmentLocations != null && !shipmentLocations.isEmpty()) {
      shipmentLocationRepository.saveAll(
        shipmentLocations.stream()
          .map(sl -> shipmentLocationMapper.toDAO(sl, booking).toBuilder()
            .location(locationService.ensureResolvable(sl.location()))
            .build())
          .toList()
      );
    }
  }
}
