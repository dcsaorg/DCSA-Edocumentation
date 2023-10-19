package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface BookingSummaryMapper {

  @Mapping(source = "bookingRequest.vessel.vesselIMONumber", target = "vesselIMONumber")
  @Mapping(target = "submissionDateTime", ignore = true)  // FIXME: Verify if this should be mapped
  @Mapping(target = "vesselName", ignore = true)  // FIXME: Verify if this should be mapped
  @Mapping(target = "exportVoyageNumber", ignore = true)  // FIXME: Verify if this should be mapped
  BookingSummaryTO BookingToBookingSummary(BookingRequest bookingRequest);
}
