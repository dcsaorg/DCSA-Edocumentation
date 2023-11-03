package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    config = EDocumentationMappingConfig.class,
    uses = {
      LocationMapper.class,
      DisplayedAddressMapper.class,
      DocumentPartyMapper.class,
      RequestedEquipmentGroupMapper.class,
      CommodityMapper.class,
      RequestedChangeMapper.class,
      ReferenceMapper.class,
      ShipmentLocationMapper.class,
    }
)
public interface BookingDataMapper {

  @Mapping(target = "requestedEquipments", ignore = true)
  @Mapping(target = "shipmentLocations", ignore = true)
  @Mapping(target = "documentParties", ignore = true)
  @Mapping(target = "id", ignore = true)
  // To make it pick up the parameter
  @Mapping(source = "voyage", target = "voyage")
  // TODO: Align names between TO and DAO
  @Mapping(source = "bookingRequestTO.placeOfBLIssue", target = "placeOfIssue")
  // Carrier-only fields - they cannot (and should not) provided by the shipper.
  // Therefore, it is fine to ignore these.
  @Mapping(target = "carrier", ignore = true)
  @Mapping(target = "termsAndConditions", ignore = true)
  @Mapping(target = "carrierClauses", ignore = true)
  @Mapping(target = "shipmentTransports", ignore = true)
  @Mapping(target = "shipmentCutOffTimes", ignore = true)
  @Mapping(target = "confirmedEquipments", ignore = true)
  @Mapping(target = "charges", ignore = true)
  @Mapping(target = "advanceManifestFilings", ignore = true)
  BookingData toDAO(BookingRequestTO bookingRequestTO, Voyage voyage, Vessel vessel);

}

