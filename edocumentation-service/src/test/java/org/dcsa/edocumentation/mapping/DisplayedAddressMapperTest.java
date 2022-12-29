package org.dcsa.edocumentation.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.DisplayedAddress;
import org.dcsa.edocumentation.service.mapping.DisplayedAddressMapper;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DisplayedAddressMapperTest {

  DisplayedAddressMapper mapper = new DisplayedAddressMapper();

  @Test
  void displayedAddressMapper_testToDAOAllLines() {
    var displayedAddresses = List.of("line1", "line2", "line3", "line4", "line5");
    DisplayedAddress displayedAddress = mapper.toDAO(displayedAddresses);
    assertEquals("line1", displayedAddress.getAddressLine1());
    assertEquals("line2", displayedAddress.getAddressLine2());
    assertEquals("line3", displayedAddress.getAddressLine3());
    assertEquals("line4", displayedAddress.getAddressLine4());
    assertEquals("line5", displayedAddress.getAddressLine5());
  }

  @Test
  void displayedAddressMapper_testToDAOOneLine() {
    var displayedAddresses = List.of("line1");
    DisplayedAddress displayedAddress = mapper.toDAO(displayedAddresses);
    assertEquals("line1", displayedAddress.getAddressLine1());
    assertNull(displayedAddress.getAddressLine2());
  }

  @Test
  void displayedAddressMapper_testToDAOTooManyLines() {
    var displayedAddresses = List.of("line1", "line2", "line3", "line4", "line5", "line6");

    Exception exception = assertThrows(ConcreteRequestErrorMessageException.class, () -> mapper.toDAO(displayedAddresses));
    assertEquals("Display Address must be max five lines.", exception.getMessage());
  }
}
