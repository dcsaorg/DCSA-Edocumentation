package org.dcsa.edocumentation.transferobjects.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShippingInstructionActiveReeferSettingsTOTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shippingInstructionActiveReeferSettingsTest_unmarshalFreezer() throws JsonProcessingException {
    String freezer = """
      {
        "type": "FREZ",
        "productName": "FROZEN",
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "temperatureSetpoint": -15,
        "temperatureUnit": "CEL"
      }
      """;

    ShippingInstructionActiveReeferSettingsTO freezerTO = objectMapper.readValue(freezer, ShippingInstructionActiveReeferSettingsTO.class);
    assertTrue(freezerTO instanceof ShippingInstructionActiveReeferSettingsTO.EblFreezerTO);
  }

  @Test
  void shippingInstructionActiveReeferSettingsTest_unmarshalSuperFreezer() throws JsonProcessingException {
    String superFreezer = """
      {
        "type": "SUPR",
        "productName": "FROZEN",
        "isCargoProbe1Required": false,
        "isCargoProbe2Required": false,
        "isCargoProbe3Required": false,
        "isCargoProbe4Required": false,
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "temperatureSetpoint": -15,
        "temperatureUnit": "CEL"
      }
      """;

    ShippingInstructionActiveReeferSettingsTO superFreezerTO = objectMapper.readValue(superFreezer, ShippingInstructionActiveReeferSettingsTO.class);
    assertTrue(superFreezerTO instanceof ShippingInstructionActiveReeferSettingsTO.EblSuperFreezerTO);
  }

  @Test
  void shippingInstructionActiveReeferSettingsTest_unmarshalRefrigerated() throws JsonProcessingException {
    String refrigerated = """
      {
        "type": "REFR",
        "productName": "FROZEN",
        "isVentilationOpen": true,
        "isCargoProbe1Required": false,
        "isCargoProbe2Required": false,
        "isCargoProbe3Required": false,
        "isCargoProbe4Required": false,
        "isDrainholesOpen": true,
        "isBulbMode":false,
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "setpoints": [{
          "temperature": "12"
        }]
      }
      """;

    ShippingInstructionActiveReeferSettingsTO refrigeratedTO = objectMapper.readValue(refrigerated, ShippingInstructionActiveReeferSettingsTO.class);
    assertTrue(refrigeratedTO instanceof ShippingInstructionActiveReeferSettingsTO.EblRefrigeratedTO);
  }

  @Test
  void shippingInstructionActiveReeferSettingsTest_controlledAtmosphere() throws JsonProcessingException {
    String controlledAtmosphere = """
      {
        "type": "CONA",
        "productName": "FROZEN",
        "isCargoProbe1Required": false,
        "isCargoProbe2Required": false,
        "isCargoProbe3Required": false,
        "isCargoProbe4Required": false,
        "isTracingRequired": true,
        "isMonitoringRequired": true,
        "extraMaterial": "spacers",
        "setpoints": [{
          "o2": "12"
        }]
      }
      """;

    ShippingInstructionActiveReeferSettingsTO controlledAtmosphereTO = objectMapper.readValue(controlledAtmosphere, ShippingInstructionActiveReeferSettingsTO.class);
    assertTrue(controlledAtmosphereTO instanceof ShippingInstructionActiveReeferSettingsTO.EblControlledAtmosphereTO);
  }
}
