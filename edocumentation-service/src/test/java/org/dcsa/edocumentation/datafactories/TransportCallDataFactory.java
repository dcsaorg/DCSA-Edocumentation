package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.TransportCall;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.domain.persistence.entity.enums.FacilityTypeCode;
import org.dcsa.skernel.domain.persistence.entity.enums.PortCallStatusCode;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@UtilityClass
public class TransportCallDataFactory {

  public TransportCall getLoadTransportCall() {
    return TransportCall.builder()
        .vessel(VesselDataFactory.vessel())
        .exportVoyage(VoyageDataFactory.voyage())
        .eventDateTimeArrival(OffsetDateTime.of(2022, 12, 25, 12, 0, 0, 0, ZoneOffset.UTC))
        .id(UUID.randomUUID())
        .transportCallReference("TC_REF_02")
        .transportCallSequenceNumber(2)
        .facilityTypeCode(FacilityTypeCode.BRTH)
        .location(Location.builder().build())
        .modeOfTransportCode("VESSEL")
        .portCallStatusCode(PortCallStatusCode.PHIN)
        .portVisitReference("port visit reference")
        .build();
  }

  public TransportCall getDischargeTransportCall() {
    return TransportCall.builder()
        .vessel(Vessel.builder().build())
        .importVoyage(VoyageDataFactory.voyage())
        .eventDateTimeArrival(OffsetDateTime.of(2023, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC))
        .id(UUID.randomUUID())
        .transportCallReference("TC_REF_01")
        .transportCallSequenceNumber(1)
        .facilityTypeCode(FacilityTypeCode.BRTH)
        .location(Location.builder().build())
        .modeOfTransportCode("VESSEL")
        .portCallStatusCode(PortCallStatusCode.PHIN)
        .portVisitReference("port visit reference")
        .build();
  }
}
