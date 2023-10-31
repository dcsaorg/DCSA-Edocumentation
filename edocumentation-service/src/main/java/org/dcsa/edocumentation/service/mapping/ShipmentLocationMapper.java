package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class, uses = LocationMapper.class)
public interface ShipmentLocationMapper {

  @Mapping(target = "id", ignore = true) /* It defaults to pulling bookingData.getId(), which is wrong */
  @Mapping(source = "bookingData", target = "bookingData")
  ShipmentLocation toDAO(ShipmentLocationTO shipmentLocationTO, BookingData bookingData);

  ShipmentLocationTO toDTO(ShipmentLocation shipmentLocation);
}
