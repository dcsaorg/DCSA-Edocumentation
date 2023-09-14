package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.service.mapping.TransportDocumentMapper;
import org.dcsa.edocumentation.transferobjects.TransportDocumentRefStatusTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.transferobjects.enums.CarrierCodeListProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransportDocumentService {

  private final TransportDocumentRepository transportDocumentRepository;
  private final TransportDocumentMapper transportDocumentMapper;

  @Transactional
  public Optional<TransportDocumentTO> findByReference(String transportDocumentReference) {
    return transportDocumentRepository
        .findByTransportDocumentReferenceAndValidUntilIsNull(transportDocumentReference)
        .map(
            transportDocument -> {
              TransportDocumentTO transportDocumentTO =
                  transportDocumentMapper.toDTO(transportDocument);

              // FIXME: Go through DAO -> TO mapping with new structure
              return resolveCarrierCodeListProvider(
                      transportDocumentTO.toBuilder(), transportDocument.getCarrier())
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
    assert carrier != null;
    if (carrier.getSmdgCode() != null) {
      builder.carrierCodeListProvider(CarrierCodeListProvider.SMDG);
      builder.carrierCode(carrier.getSmdgCode());
    } else if (carrier.getNmftaCode() != null) {
      builder.carrierCodeListProvider(CarrierCodeListProvider.NMFTA);
      builder.carrierCode(carrier.getNmftaCode());
    }
    return builder;
  }

  public Optional<TransportDocumentRefStatusTO> approveTransportDocument(String transportDocumentReference) {
    TransportDocument document = transportDocumentRepository
      .findByTransportDocumentReferenceAndValidUntilIsNull(transportDocumentReference)
      .orElse(null);
    if (document == null) {
      return Optional.empty();
    }
    document.approveFromShipper();

    transportDocumentRepository.save(document);
    return Optional.of(transportDocumentMapper.toStatusDTO(document));
  }
}
