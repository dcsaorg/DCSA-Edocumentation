package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.ShipmentTransportDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.ModeOfTransport;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentTransportRepository;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransportServiceTest {

  @Mock ModeOfTransportService modeOfTransportService;
  @Mock ShipmentTransportRepository shipmentTransportRepository;
  @Mock LocationMapper locationMapper;

  @InjectMocks TransportService transportService;

  @Test
  void transportServiceTest_findTransportService() {
    ShipmentTransport expectedShipmentTransport = ShipmentTransportDataFactory.shipmentTransport();

    when(shipmentTransportRepository.findByShipmentID(any()))
        .thenReturn(List.of(expectedShipmentTransport));
    when(modeOfTransportService.resolveModeOfTransport("VESSEL"))
        .thenReturn(
            ModeOfTransport.builder()
                .dcsaTransportType(
                    org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType
                        .VESSEL)
                .code("test_code")
                .build());
    List<TransportTO> transports = transportService.findTransportByShipmentId(UUID.randomUUID());

    assertEquals(1, transports.size());
    assertEquals(DCSATransportType.VESSEL, transports.get(0).modeOfTransport());
    assertEquals("voyageRef", transports.get(0).carrierImportVoyageNumber());
    assertEquals(
        expectedShipmentTransport.getTransport().getLoadTransportCall().getEventDateTimeDeparture(),
        transports.get(0).plannedDepartureDate());
    assertEquals(
        expectedShipmentTransport
            .getTransport()
            .getDischargeTransportCall()
            .getEventDateTimeArrival(),
        transports.get(0).plannedArrivalDate());
  }

  @Test
  void transportServiceTest_findMultipleShipmentTransports() {
    List<ShipmentTransport> expectedShipmentTransports =
        ShipmentTransportDataFactory.shipmentTransports();

    when(shipmentTransportRepository.findByShipmentID(any()))
        .thenReturn(expectedShipmentTransports);
    when(modeOfTransportService.resolveModeOfTransport("VESSEL"))
        .thenReturn(
            ModeOfTransport.builder()
                .dcsaTransportType(
                    org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType
                        .VESSEL)
                .code("test_code")
                .build());
    List<TransportTO> transports = transportService.findTransportByShipmentId(UUID.randomUUID());

    assertEquals(2, transports.size());
    assertEquals(DCSATransportType.VESSEL, transports.get(0).modeOfTransport());
    assertEquals("voyageRef", transports.get(0).carrierImportVoyageNumber());
    assertEquals(1, transports.get(0).transportPlanStageSequenceNumber());
    assertEquals(
        expectedShipmentTransports
            .get(0)
            .getTransport()
            .getLoadTransportCall()
            .getEventDateTimeDeparture(),
        transports.get(0).plannedDepartureDate());
    assertEquals(
        expectedShipmentTransports
            .get(0)
            .getTransport()
            .getDischargeTransportCall()
            .getEventDateTimeArrival(),
        transports.get(0).plannedArrivalDate());
  }

  @Test
  void transportServiceTest_noShipmentTransportFound() {
    when(shipmentTransportRepository.findByShipmentID(any())).thenReturn(Collections.emptyList());

    List<TransportTO> transports = transportService.findTransportByShipmentId(UUID.randomUUID());

    assertEquals(0, transports.size());
  }
}
