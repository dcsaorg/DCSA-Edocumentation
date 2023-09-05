package org.dcsa.edocumentation.service.mapping;

import java.time.LocalDate;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.edocumentation.transferobjects.TDTransportTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentRefStatusTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.transferobjects.enums.CarrierCodeListProvider;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    LocationMapper.class,
    DocumentPartyMapper.class,
    DisplayedAddressMapper.class,
    ConsignmentItemMapper.class,
  })
public abstract class TransportDocumentMapper {

  @Autowired
  protected LocationMapper locMapper;

  @Autowired
  protected ModeOfTransportMapper modeOfTransportMapper;

  @Mapping(source = "transportDocument.shippingInstruction.documentStatus", target = "documentStatus")
  public abstract TransportDocumentRefStatusTO toStatusDTO(TransportDocument transportDocument);


  /*
      There is a list of fields that must be the same if multiple bookings are being linked to from the same SI:

      transportPlan
      shipmentLocations
      receiptTypeAtOrigin
      deliveryTypeAtDestination
      cargoMovementTypeAtOrigin
      cargoMovementTypeAtDestination
      serviceContractReference
      termsAndConditions
      Invoice Payable At (if provided)
      Place of B/L Issuance (if provided)
      Transport Document Type Code (if provided)

      This means that we can just pick *any* of the bookings/shipments when resolving these fields for the
      purpose of figuring out the value.
   */
  @Mapping(source = "transportDocument.shippingInstruction.consignmentItems", target = "consignmentItems")
  @Mapping(source = "transportDocument.shippingInstruction.documentParties", target = "documentParties")
  @Mapping(source = "transportDocument.shippingInstruction.references", target = "references")
  @Mapping(source = "transportDocument.shippingInstruction.isElectronic", target = "isElectronic")
  @Mapping(source = "transportDocument.shippingInstruction.isToOrder", target = "isToOrder")
  @Mapping(source = "transportDocument.shippingInstruction.documentStatus", target = "documentStatus")
  @Mapping(source = "transportDocument.shippingInstruction.transportDocumentTypeCode", target = "transportDocumentTypeCode")

  @Mapping(source = "transportDocument.shippingInstruction.displayedNameForPlaceOfReceipt", target = "displayedNameForPlaceOfReceipt")
  @Mapping(source = "transportDocument.shippingInstruction.displayedNameForPortOfLoad", target = "displayedNameForPortOfLoad")
  @Mapping(source = "transportDocument.shippingInstruction.displayedNameForPortOfDischarge", target = "displayedNameForPortOfDischarge")
  @Mapping(source = "transportDocument.shippingInstruction.displayedNameForPlaceOfDelivery", target = "displayedNameForPlaceOfDelivery")

  @Mapping(source = "transportDocument.shippingInstruction.numberOfCopiesWithCharges", target = "numberOfCopiesWithCharges")
  @Mapping(source = "transportDocument.shippingInstruction.numberOfCopiesWithoutCharges", target = "numberOfCopiesWithoutCharges")
  @Mapping(source = "transportDocument.shippingInstruction.numberOfOriginalsWithCharges", target = "numberOfOriginalsWithCharges")
  @Mapping(source = "transportDocument.shippingInstruction.numberOfOriginalsWithoutCharges", target = "numberOfOriginalsWithoutCharges")

  @Mapping(target = "transports", expression = "java(mapSIToTransports(transportDocument.getShippingInstruction()))")
  @Mapping(target = "carrierCode", expression = "java(carrierCode(transportDocument))")
  @Mapping(target = "carrierCodeListProvider", expression = "java(carrierCodeListProvider(transportDocument))")

  @Mapping(target = "termsAndConditions", expression = "java(termsAndConditions(transportDocument))")
  @Mapping(target = "serviceContractReference", expression = "java(serviceContractReference(transportDocument))")
  @Mapping(target = "contractQuotationReference", expression = "java(contractQuotationReference(transportDocument))")
  @Mapping(target = "declaredValue", ignore = true)  // FIXME: We should be mapping this field.
  @Mapping(target = "declaredValueCurrency", ignore = true)  // FIXME: We should be mapping this field.

  @Mapping(target = "receiptTypeAtOrigin", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME!
  @Mapping(target = "deliveryTypeAtDestination", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME!
  @Mapping(target = "cargoMovementTypeAtOrigin", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME!
  @Mapping(target = "cargoMovementTypeAtDestination", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME!
  @Mapping(target = "utilizedTransportEquipments", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME!
  @Mapping(source = "transportDocument.shippingInstruction.invoicePayableAt", target = "invoicePayableAt")
  public abstract TransportDocumentTO toDTO(TransportDocument transportDocument);

  protected Shipment resolveAnyShipment(TransportDocument document) {
    return document.getShippingInstruction().getConsignmentItems().iterator().next().getShipment();
  }

  protected String termsAndConditions(TransportDocument document) {
    return resolveAnyShipment(document).getTermsAndConditions();
  }

  protected String serviceContractReference(TransportDocument document) {
    return resolveAnyShipment(document).getBooking().getServiceContractReference();
  }

  protected String contractQuotationReference(TransportDocument document) {
    return resolveAnyShipment(document).getBooking().getContractQuotationReference();
  }

  protected CarrierCodeListProvider carrierCodeListProvider(TransportDocument transportDocument) {
    var carrier = transportDocument.getCarrier();
    if (carrier.getSmdgCode() != null) {
      return CarrierCodeListProvider.SMDG;
    }
    assert carrier.getNmftaCode() != null;
    return CarrierCodeListProvider.NMFTA;
  }

  protected String carrierCode(TransportDocument transportDocument) {
    var carrier = transportDocument.getCarrier();
    if (carrier.getSmdgCode() != null) {
      return carrier.getSmdgCode();
    }
    assert carrier.getNmftaCode() != null;
    return carrier.getNmftaCode();
  }

  private boolean isSameLocation(Location lhs, Location rhs) {
    if (rhs == null) {
      return false;
    }
    if (lhs.getAddress() != null) {
      return lhs.getAddress().equals(rhs.getAddress());
    }
    if (lhs.getUNLocationCode() == null) {
      throw new IllegalStateException("Location must have either an Address or a UNLocationCode");
    }
    return lhs.getUNLocationCode().equals(rhs.getUNLocationCode());
  }

  protected TDTransportTO mapSIToTransports(ShippingInstruction shippingInstruction) {
    var shipment = shippingInstruction.getConsignmentItems().iterator().next().getShipment();
    var shipmentLocations = shipment.getShipmentLocations();
    var shipmentTransports = shipment.getShipmentTransports();

    var preLoc = findLocation(shipmentLocations, LocationType.PRE);
    var polLoc = findLocation(shipmentLocations, LocationType.POL);
    var podLoc = findLocation(shipmentLocations, LocationType.POD);
    var pdeLoc = findLocation(shipmentLocations, LocationType.PDE);

    LocalDate departureDate = null;
    LocalDate arrivalDate = null;
    DCSATransportType preCarriedBy = null;

    for (var st : shipmentTransports) {
      var loadLoc = st.getLoadLocation();
      var dischargeLoc = st.getDischargeLocation();
      if (isSameLocation(loadLoc, preLoc) || isSameLocation(loadLoc, polLoc)) {
        var date = st.getPlannedDepartureDate();
        if (departureDate == null || departureDate.isAfter(date)) {
          departureDate = date;
        }
        if (isSameLocation(loadLoc, preLoc)) {
          preCarriedBy = modeOfTransportMapper.toTO(st.getModeOfTransport());
        }
      }
      if (isSameLocation(dischargeLoc, podLoc) || isSameLocation(dischargeLoc, pdeLoc)) {
        var date = st.getPlannedArrivalDate();
        if (arrivalDate == null || arrivalDate.isBefore(date)) {
          arrivalDate = date;
        }
      }
    }
    return TDTransportTO.builder()
        .plannedArrivalDate(arrivalDate)
        .plannedDepartureDate(departureDate)
        .preCarriedBy(preCarriedBy)
        .placeOfReceipt(locMapper.toDTO(preLoc))
        .portOfLoading(locMapper.toDTO(polLoc))
        .portOfDischarge(locMapper.toDTO(podLoc))
        .placeOfDelivery(locMapper.toDTO(pdeLoc))
        .onwardInlandRouting(locMapper.toDTO(findLocation(shipmentLocations, LocationType.OIR)))
        .build();
  }

  protected Location findLocation(Iterable<ShipmentLocation> shipmentLocations, LocationType locationType) {
    ShipmentLocation found = null;
    for (var sl : shipmentLocations) {
      if (sl.getShipmentLocationTypeCode().equals(locationType.name())) {
        found = sl;
        break;
      }
    }
    if (found == null) {
      return null;
    }
    return found.getLocation();
  }
}
