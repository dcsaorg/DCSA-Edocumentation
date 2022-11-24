package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.transferobjects.TransportDocumentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.CarrierCodeListProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PartyMapper.class})
public abstract class TransportDocumentSummaryMapper {

  @Mappings(
      value = {
        @Mapping(
            target = "shippingInstructionReference",
            source = "transportDocument.shippingInstruction.shippingInstructionReference"),
        @Mapping(
            target = "documentStatus",
            source = "transportDocument.shippingInstruction.documentStatus"),
        @Mapping(target = "shippedOnBoardDate", source = "shippedOnBoardDate"),
        @Mapping(target = "carrierCode", source = "transportDocument"),
        @Mapping(target = "carrierCodeListProvider", source = "transportDocument"),
        @Mapping(target = "carrierBookingReferences", source = "transportDocument")
      })
  public abstract TransportDocumentSummaryTO toTO(TransportDocument transportDocument);

  List<String> mapCarrierBookingReferences(TransportDocument td) {
    return td.getShippingInstruction().getConsignmentItems().stream()
        .map(
            c ->
                c.getShipment()
                    .getCarrierBookingReference()) // consignment item will always have a shipment,
        // based on the IM
        .toList();
  }

  String mapCarrierCode(TransportDocument transportDocument) {
    // smdg code is given preference over nmfta code
    if (transportDocument.getCarrier().getSmdgCode() != null) {
      return transportDocument.getCarrier().getSmdgCode();
    } else {
      return transportDocument.getCarrier().getNmftaCode();
    }
  }

  CarrierCodeListProvider mapCarrierCodeListProvider(TransportDocument transportDocument) {
    // smdg code is given preference over nmfta code
    if (transportDocument.getCarrier().getSmdgCode() != null) {
      return CarrierCodeListProvider.SMDG;
    } else {
      return CarrierCodeListProvider.NMFTA;
    }
  }
}
