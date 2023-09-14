package org.dcsa.edocumentation.service.mapping;


import org.dcsa.edocumentation.transferobjects.enums.CargoMovementType;
import org.dcsa.edocumentation.transferobjects.enums.ReceiptDeliveryType;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        config = EDocumentationMappingConfig.class
)
public interface EnumMapper {
    ReceiptDeliveryType toTO(org.dcsa.edocumentation.domain.persistence.entity.enums.ReceiptDeliveryType value);
    CargoMovementType toTO(org.dcsa.edocumentation.domain.persistence.entity.enums.CargoMovementType value);
}
