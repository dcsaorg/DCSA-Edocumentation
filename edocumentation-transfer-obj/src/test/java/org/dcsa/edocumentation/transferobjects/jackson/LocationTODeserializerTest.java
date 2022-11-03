package org.dcsa.edocumentation.transferobjects.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.text.SimpleDateFormat;

public class LocationTODeserializerTest {
  public final String addressLocationFile = "recordWithAddressLocation.json";
  public final String facilityLocationFile = "recordWithFacilityLocation.json";
  public final String unLocationLocationFile = "recordWithUNLocationLocation.json";
  public final String invalidFile = "invalidRecordWithAddressAndUNLocation.json";

  public record RecordWithLocation(LocationTO location) {}

  private ObjectMapper objectMapper =
    new ObjectMapper()
      .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL)
      .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
      .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .findAndRegisterModules();

  @Test
  public void testValidCombinations() throws Exception {
    deserializeFromFile(addressLocationFile);
    deserializeFromFile(facilityLocationFile);
    deserializeFromFile(unLocationLocationFile);
  }

  @Test
  public void testInvalidCombinations() {
    testInvalidCombo(invalidFile, JsonMappingException.class);
  }

  private <T extends Exception> void testInvalidCombo(String fileName, Class<T> expectedType) {
    Assertions.assertThrowsExactly(expectedType, () ->
      deserializeFromFile(fileName)
    );
  }

  private void deserializeFromFile(String fileName) throws Exception {
    URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
    objectMapper.readValue(url, RecordWithLocation.class);
  }
}
