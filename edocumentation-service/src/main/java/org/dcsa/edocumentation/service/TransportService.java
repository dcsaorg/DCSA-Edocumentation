package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.TransportCall;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentTransportRepository;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.enums.TransportPlanStageCode;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransportService {

  private final ShipmentTransportRepository shipmentTransportRepository;
  private final LocationMapper locationMapper;
  private final ModeOfTransportService modeOfTransportService;

  @Transactional(Transactional.TxType.MANDATORY)
  public List<TransportTO> findTransportByShipmentId(UUID shipmentId) {
    return shipmentTransportRepository.findByShipmentID(shipmentId).stream()
        .map(
            shipmentTransport -> {
              TransportTO.TransportTOBuilder builder =
                  setLoadTransportCall(
                      TransportTO.builder(),
                      shipmentTransport.getTransport().getLoadTransportCall());
              return setDischargeTransportCall(
                      builder, shipmentTransport.getTransport().getDischargeTransportCall())
                  .transportPlanStage(
                      TransportPlanStageCode.valueOf(
                          shipmentTransport.getTransportPlanStageCode().name()))
                  .transportPlanStageSequenceNumber(
                      shipmentTransport.getTransportPlanStageSequenceNumber())
                  .isUnderShippersResponsibility(
                      shipmentTransport.isUnderShippersResponsibility())
                  .build();
            })
        .sorted(Comparator.comparingInt(TransportTO::transportPlanStageSequenceNumber))
        .toList();
  }

  private TransportTO.TransportTOBuilder setLoadTransportCall(
      TransportTO.TransportTOBuilder builder, TransportCall loadTransportCall) {
    Voyage exportVoyage = loadTransportCall.getExportVoyage();
    Vessel vessel = loadTransportCall.getVessel();

    if (vessel != null) {
      builder.vesselIMONumber(vessel.getVesselIMONumber()).vesselName(vessel.getName());
    }

    return builder
        .loadLocation(locationMapper.toDTO(loadTransportCall.getLocation()))
        .plannedDepartureDate(loadTransportCall.getEventDateTimeDeparture())
        .modeOfTransport(
            DCSATransportType.valueOf(
                modeOfTransportService
                    .resolveModeOfTransport(loadTransportCall.getModeOfTransportCode())
                    .getDcsaTransportType()
                    .name()))
        .carrierExportVoyageNumber(exportVoyage.getCarrierVoyageNumber())
        .universalExportVoyageReference(exportVoyage.getUniversalVoyageReference());
  }

  private TransportTO.TransportTOBuilder setDischargeTransportCall(
      TransportTO.TransportTOBuilder builder, TransportCall dischargeTransportCall) {
    Voyage importVoyage = dischargeTransportCall.getImportVoyage();

    return builder
        .dischargeLocation(locationMapper.toDTO(dischargeTransportCall.getLocation()))
        .plannedArrivalDate(dischargeTransportCall.getEventDateTimeArrival())
        .carrierImportVoyageNumber(importVoyage.getCarrierVoyageNumber())
        .universalImportVoyageReference(importVoyage.getUniversalVoyageReference());
  }
}
