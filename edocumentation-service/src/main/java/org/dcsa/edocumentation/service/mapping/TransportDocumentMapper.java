package org.dcsa.edocumentation.service.mapping;

import java.time.LocalDate;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.edocumentation.transferobjects.TDTransportTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.unofficial.TransportDocumentRefStatusTO;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {LocationMapper.class, DocumentPartyMapper.class, CargoItemMapper.class})
public abstract class TransportDocumentMapper {

  @Autowired
  protected LocationMapper locMapper;

  @Mapping(source = "transportDocument.shippingInstruction.documentStatus", target = "documentStatus")
  public abstract TransportDocumentRefStatusTO toStatusDTO(TransportDocument transportDocument);

  @Mapping(source = "transportDocument.shippingInstruction.consignmentItems", target = "consignmentItems")
  @Mapping(source = "transportDocument.shippingInstruction.documentParties", target = "documentParties")
  @Mapping(source = "transportDocument.shippingInstruction.references", target = "references")
  @Mapping(source = "transportDocument.shippingInstruction.isElectronic", target = "isElectronic")
  @Mapping(source = "transportDocument.shippingInstruction.isToOrder", target = "isToOrder")
  @Mapping(source = "transportDocument.shippingInstruction.documentStatus", target = "documentStatus")
  @Mapping(target = "transports", expression = "java(mapSIToTransports(transportDocument.getShippingInstruction()))")
  // TODO: Go over this mapping and ensure all fields are covered.
  public abstract TransportDocumentTO toDTO(TransportDocument transportDocument);

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
      var loadTC = st.getTransport().getLoadTransportCall();
      var dischargeTC = st.getTransport().getDischargeTransportCall();
      if (isSameLocation(loadTC.getLocation(), preLoc) || isSameLocation(loadTC.getLocation(), polLoc)) {
        var date = loadTC.getEventDateTimeDeparture().toLocalDate();
        if (departureDate == null || departureDate.isAfter(date)) {
          departureDate = date;
        }
        if (isSameLocation(loadTC.getLocation(), preLoc)) {
          // FIXME: DCSATransportType.valueOf(loadTC.getModeOfTransportCode());
          // modeOfTransportCode is the internal code and
          preCarriedBy = null;
        }
      }
      if (isSameLocation(dischargeTC.getLocation(), podLoc) || isSameLocation(dischargeTC.getLocation(), pdeLoc)) {
        var date = dischargeTC.getEventDateTimeArrival().toLocalDate();
        if (arrivalDate == null || arrivalDate.isBefore(date)) {
          arrivalDate = date;
        }
      }
    }

    // TODO: Compute plannedArrivalDate, plannedDepartureDate and preCarriedBy from
    //  shipment.getShipmentTransports()
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
      if (sl.getShipmentLocationTypeCode() == locationType) {
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
