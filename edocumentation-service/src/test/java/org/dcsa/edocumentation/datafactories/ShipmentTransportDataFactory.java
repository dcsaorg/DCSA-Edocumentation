package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.CommercialVoyage;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.domain.persistence.entity.Transport;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportPlanStageCode;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class ShipmentTransportDataFactory {

  public ShipmentTransport shipmentTransport() {
    return ShipmentTransport.builder()
      .id(UUID.randomUUID())
      .shipmentID(UUID.randomUUID())
      .transportPlanStageSequenceNumber(1)
      .transportPlanStageCode(TransportPlanStageCode.ONC)
      .commercialVoyage(
        CommercialVoyage.builder()
          .commercialVoyageId(UUID.randomUUID())
          .commercialVoyageName("commercial voyage name")
          .build())
      .transport(
        Transport.builder()
          .dischargeTransportCall(TransportCallDataFactory.getDischargeTransportCall())
          .loadTransportCall(TransportCallDataFactory.getLoadTransportCall())
          .transportReference("transportRef")
          .build())
      .isUnderShippersResponsibility(true)
      .build();
  }

  public static List<ShipmentTransport> shipmentTransports() {
    ShipmentTransport secondShipmentTransport = ShipmentTransport.builder()
      .id(UUID.randomUUID())
      .shipmentID(UUID.randomUUID())
      .transportPlanStageSequenceNumber(2)
      .transportPlanStageCode(TransportPlanStageCode.MNC)
      .commercialVoyage(
        CommercialVoyage.builder()
          .commercialVoyageId(UUID.randomUUID())
          .commercialVoyageName("commercial voyage name")
          .build())
      .transport(
        Transport.builder()
          .dischargeTransportCall(TransportCallDataFactory.getDischargeTransportCall())
          .loadTransportCall(TransportCallDataFactory.getLoadTransportCall())
          .transportReference("transportRef2")
          .build())
      .isUnderShippersResponsibility(true)
      .build();

    return List.of(shipmentTransport(), secondShipmentTransport);
  }
}
