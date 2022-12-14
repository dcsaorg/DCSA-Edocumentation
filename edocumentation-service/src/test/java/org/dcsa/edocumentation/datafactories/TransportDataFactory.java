package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.enums.TransportPlanStageCode;

import java.time.OffsetDateTime;

@UtilityClass
public class TransportDataFactory {

  public TransportTO singleTransportTO() {
    return TransportTO.builder()
      .transportPlanStage(TransportPlanStageCode.MNC)
      .carrierExportVoyageNumber("123N")
      .universalExportVoyageReference("1234N")
      .carrierImportVoyageNumber("123S")
      .universalImportVoyageReference("1234S")
      .plannedDepartureDate(OffsetDateTime.now())
      .plannedArrivalDate(OffsetDateTime.now())
      .vesselName("dummy vessel")
      .vesselIMONumber("012345676")
      .modeOfTransport(DCSATransportType.VESSEL)
      .transportPlanStageSequenceNumber(1)
      .loadLocation(LocationDataFactory.facilityLocationTO())
      .dischargeLocation(LocationDataFactory.unLocationLocationTO())
      .isUnderShippersResponsibility(false)
      .build();
  }
}
