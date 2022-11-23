package org.dcsa.edocumentation.transferobjects.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingActiveReeferSettingsTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void bookingActiveReeferSettingsTOTest_unmarshalSuperFreezer() throws JsonProcessingException {
    String superFreezer = """
    {
        "type": "SUPR",
        "productName": "FROZEN",
        "isGeneratorSetRequired": true,
        "isPreCoolingRequired": true,
        "isColdTreatmentRequired": true,
        "isHotStuffingAllowed": true,
        "isHighValueCargo": true,
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "temperatureSetpoint": -60,
        "temperatureUnit": "CEL"
      }
    """;
    BookingActiveReeferSettingsTO superFreezerTO = objectMapper.readValue(superFreezer, BookingActiveReeferSettingsTO.class);
    assertTrue(superFreezerTO instanceof BookingActiveReeferSettingsTO.BkgSuperFreezerTO );
  }

  @Test
  void bookingActiveReeferSettingsTOTest_unmarshalFreezer() throws JsonProcessingException {
    String freezer = """
    {
        "type": "FREZ",
        "productName": "FROZEN",
        "isGeneratorSetRequired": true,
        "isPreCoolingRequired": true,
        "isColdTreatmentRequired": true,
        "isHotStuffingAllowed": true,
        "isHighValueCargo": true,
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "temperatureSetpoint": -15,
        "temperatureUnit": "CEL"
      }
     """ ;
    BookingActiveReeferSettingsTO superFreezerTO = objectMapper.readValue(freezer, BookingActiveReeferSettingsTO.class);
    assertTrue(superFreezerTO instanceof BookingActiveReeferSettingsTO.BkgFreezerTO );
  }

  @Test
  void bookingActiveReeferSettingsTOTest_unmarshalRefrigerated() throws JsonProcessingException {
    String freezer = """
    {
        "type": "REFR",
        "productName": "Controlled atmosphere",
        "isGeneratorSetRequired": true,
        "isPreCoolingRequired": true,
        "isColdTreatmentRequired": true,
        "isHotStuffingAllowed": true,
        "isHighValueCargo": true,
        "isVentilationOpen": false,
        "isCargoProbe1Required": true,
        "isCargoProbe2Required": true,
        "isCargoProbe3Required": true,
        "isCargoProbe4Required": true,
        "isDrainholesOpen": false,
        "isBulbMode": false,
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "setpoints": [{
          "temperature": "12"
        }]
      }
     """ ;
    BookingActiveReeferSettingsTO superFreezerTO = objectMapper.readValue(freezer, BookingActiveReeferSettingsTO.class);
    assertTrue(superFreezerTO instanceof BookingActiveReeferSettingsTO.BkgRefrigeratedTO );
  }

  @Test
  void bookingActiveReeferSettingsTOTest_unmarshalControlledAtmosphere() throws JsonProcessingException {
    String freezer = """
    {
        "type": "CONA",
        "productName": "Controlled atmosphere",
        "isGeneratorSetRequired": true,
        "isPreCoolingRequired": true,
        "isColdTreatmentRequired": true,
        "isHotStuffingAllowed": true,
        "isHighValueCargo": true,
        "isCargoProbe1Required": true,
        "isCargoProbe2Required": true,
        "isCargoProbe3Required": true,
        "isCargoProbe4Required": true,
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "setpoints": [{
          "temperature": "12",
          "o2": "12.3"
        }]
      }
     """ ;
    BookingActiveReeferSettingsTO superFreezerTO = objectMapper.readValue(freezer, BookingActiveReeferSettingsTO.class);
    assertTrue(superFreezerTO instanceof BookingActiveReeferSettingsTO.BkgControlledAtmosphereTO );
  }

}
