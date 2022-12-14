package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.service.mapping.TransportDocumentMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.edocumentation.transferobjects.enums.CarrierCodeListProvider;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransportDocumentService {

  private final TransportDocumentRepository transportDocumentRepository;
  private final TransportService transportService;
  private final ShipmentLocationService shipmentLocationService;
  private final TransportDocumentMapper transportDocumentMapper;

  @Transactional
  public Optional<TransportDocumentTO> findByReference(String transportDocumentReference) {
    return transportDocumentRepository
        .findByTransportDocumentReferenceAndValidUntilIsNull(transportDocumentReference)
        .map(
            transportDocument -> {
              TransportDocumentTO transportDocumentTO =
                  transportDocumentMapper.toDTO(transportDocument);

              return resolveCarrierCodeListProvider(
                      transportDocumentTO.toBuilder(), transportDocument.getCarrier())
                  .transports(getTransports(transportDocument.getShippingInstruction()))
                  .shipmentLocations(
                      getShipmentLocations(transportDocument.getShippingInstruction()))
                  .termsAndConditions(
                    getTermsAndConditions(transportDocument))
                  .declaredValue(
                    getDeclaredValue(transportDocument))
                  .declaredValueCurrency(
                    getDeclaredValueCurrency(transportDocument))
                  .serviceContractReference(
                    getServiceContractReference(transportDocument))
                  .build();
            });
  }

  private String getTermsAndConditions(TransportDocument transportDocument) {
    return Optional.ofNullable(transportDocument.getShippingInstruction())
      .map(ShippingInstruction::retrieveOneShipment)
      .map(Shipment::getTermsAndConditions)
      .orElse(null);
  }

  private Float getDeclaredValue(TransportDocument transportDocument) {
    return getBooking(transportDocument)
      .map(Booking::getDeclaredValue)
      .orElse(null);
  }

  private String getDeclaredValueCurrency(TransportDocument transportDocument) {
    return getBooking(transportDocument)
      .map(Booking::getDeclaredValueCurrency)
      .orElse(null);
  }

  private String getServiceContractReference(TransportDocument transportDocument) {
    return getBooking(transportDocument)
      .map(Booking::getServiceContractReference)
      .orElse(null);
  }

  private Optional<Booking> getBooking(TransportDocument transportDocument) {
    return Optional.ofNullable(transportDocument.getShippingInstruction())
      .map(ShippingInstruction::retrieveOneShipment)
      .map(Shipment::getBooking);
  }

  private TransportDocumentTO.TransportDocumentTOBuilder resolveCarrierCodeListProvider(
      TransportDocumentTO.TransportDocumentTOBuilder builder, Carrier carrier) {
    if (carrier != null) {
      if (carrier.getSmdgCode() != null) {
        builder.carrierCodeListProvider(CarrierCodeListProvider.SMDG);
        builder.carrierCode(carrier.getSmdgCode());
      } else if (carrier.getNmftaCode() != null) {
        builder.carrierCodeListProvider(CarrierCodeListProvider.NMFTA);
        builder.carrierCode(carrier.getNmftaCode());
      }
    } else {
      throw ConcreteRequestErrorMessageException.notFound(
          "No Carrier found on the transportdocument.");
    }
    return builder;
  }

  private List<TransportTO> getTransports(ShippingInstruction shippingInstruction) {
    if (shippingInstruction == null) {
      throw ConcreteRequestErrorMessageException.notFound(
          "No shipping instruction present for this transportDocument.");
    }
    return transportService.findTransportByShipmentId(
        shippingInstruction.retrieveOneShipment().getId());
  }

  private List<ShipmentLocationTO> getShipmentLocations(ShippingInstruction shippingInstruction) {
    return shipmentLocationService.getShipmentLocations(
        shippingInstruction.getConsignmentItems().stream()
            .map(ConsignmentItem::getShipment)
            .toList());
  }
}
