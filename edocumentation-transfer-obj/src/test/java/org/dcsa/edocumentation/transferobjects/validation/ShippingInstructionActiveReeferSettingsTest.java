package org.dcsa.edocumentation.transferobjects.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShippingInstructionActiveReeferSettingsTest {
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

    ShippingInstructionActiveReeferSettings freezerTO = objectMapper.readValue(freezer, ShippingInstructionActiveReeferSettings.class);
    assertTrue(freezerTO instanceof ShippingInstructionActiveReeferSettings.EblFreezerTO);
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

    ShippingInstructionActiveReeferSettings superFreezerTO = objectMapper.readValue(superFreezer, ShippingInstructionActiveReeferSettings.class);
    assertTrue(superFreezerTO instanceof ShippingInstructionActiveReeferSettings.EblSuperFreezerTO);
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

    ShippingInstructionActiveReeferSettings refrigeratedTO = objectMapper.readValue(refrigerated, ShippingInstructionActiveReeferSettings.class);
    assertTrue(refrigeratedTO instanceof ShippingInstructionActiveReeferSettings.EblRefrigeratedTO);
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

    ShippingInstructionActiveReeferSettings controlledAtmosphereTO = objectMapper.readValue(controlledAtmosphere, ShippingInstructionActiveReeferSettings.class);
    assertTrue(controlledAtmosphereTO instanceof ShippingInstructionActiveReeferSettings.EblControlledAtmosphereTO);
  }
}
