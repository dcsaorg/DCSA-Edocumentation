package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.datafactories.LocationDataFactory;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LocationMapperTest {
  @Mock private AddressMapper addressMapper;
  @InjectMocks private LocationMapper locationMapper;

  @Test
  public void testAddressLocationMapping() {

    LocationTO actual = locationMapper.toDTO(LocationDataFactory.addressLocationWithId());
  }
}
