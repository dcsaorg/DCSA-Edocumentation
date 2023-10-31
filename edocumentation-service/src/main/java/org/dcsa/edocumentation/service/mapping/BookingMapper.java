package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.transferobjects.BookingRequestRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.dcsa.edocumentation.transferobjects.ConfirmedBookingTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ConfirmedBookingRefStatusTO;
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
      TransportMapper.class,
    }
)
public interface BookingMapper {

  @Mapping(source = "bookingData.receiptTypeAtOrigin", target = "receiptTypeAtOrigin")
  @Mapping(source = "bookingData.deliveryTypeAtDestination", target = "deliveryTypeAtDestination")
  @Mapping(source = "bookingData.cargoMovementTypeAtOrigin", target = "cargoMovementTypeAtOrigin")
  @Mapping(source = "bookingData.cargoMovementTypeAtDestination", target = "cargoMovementTypeAtDestination")
  @Mapping(source = "bookingData.serviceContractReference", target = "serviceContractReference")
  @Mapping(source = "bookingData.declaredValue", target = "declaredValue")
  @Mapping(source = "bookingData.declaredValueCurrency", target = "declaredValueCurrency")
  @Mapping(source = "bookingData.paymentTermCode", target = "paymentTermCode")
  @Mapping(source = "bookingData.isPartialLoadAllowed", target = "isPartialLoadAllowed")
  @Mapping(source = "bookingData.isExportDeclarationRequired", target = "isExportDeclarationRequired")
  @Mapping(source = "bookingData.exportDeclarationReference", target = "exportDeclarationReference")
  @Mapping(source = "bookingData.isImportLicenseRequired", target = "isImportLicenseRequired")
  @Mapping(source = "bookingData.importLicenseReference", target = "importLicenseReference")
  @Mapping(source = "bookingData.isAMSACIFilingRequired", target = "isAMSACIFilingRequired")
  @Mapping(source = "bookingData.isDestinationFilingRequired", target = "isDestinationFilingRequired")
  @Mapping(source = "bookingData.contractQuotationReference", target = "contractQuotationReference")
  @Mapping(source = "bookingData.expectedDepartureDate", target = "expectedDepartureDate")
  @Mapping(source = "bookingData.expectedArrivalAtPlaceOfDeliveryStartDate", target = "expectedArrivalAtPlaceOfDeliveryStartDate")
  @Mapping(source = "bookingData.expectedArrivalAtPlaceOfDeliveryEndDate", target = "expectedArrivalAtPlaceOfDeliveryEndDate")
  @Mapping(source = "bookingData.transportDocumentTypeCode", target = "transportDocumentTypeCode")
  @Mapping(source = "bookingData.transportDocumentReference", target = "transportDocumentReference")
  @Mapping(source = "bookingData.bookingChannelReference", target = "bookingChannelReference")
  @Mapping(source = "bookingData.incoTerms", target = "incoTerms")
  @Mapping(source = "bookingData.communicationChannelCode", target = "communicationChannelCode")
  @Mapping(source = "bookingData.isEquipmentSubstitutionAllowed", target = "isEquipmentSubstitutionAllowed")
  @Mapping(source = "bookingData.invoicePayableAt", target = "invoicePayableAt")
  @Mapping(source = "bookingData.requestedEquipments", target = "requestedEquipments")
  @Mapping(source = "bookingData.references", target = "references")
  @Mapping(source = "bookingData.documentParties", target = "documentParties")
  @Mapping(source = "bookingData.shipmentLocations", target = "shipmentLocations")
  @Mapping(source = "bookingData.placeOfIssue", target = "placeOfBLIssue")
  @Mapping(source = "bookingData.voyage.universalVoyageReference", target = "universalExportVoyageReference")
  @Mapping(source = "bookingData.voyage.carrierVoyageNumber", target = "carrierExportVoyageNumber")
  @Mapping(source = "bookingData.voyage.service.carrierServiceCode", target = "carrierServiceCode")
  @Mapping(source = "bookingData.voyage.service.universalServiceReference", target = "universalServiceReference")
  @Mapping(source = "bookingData.vessel.vesselIMONumber", target = "vesselIMONumber")
  BookingRequestTO toDTO(Booking booking);

  @Mapping(source = "bookingData.termsAndConditions", target = "termsAndConditions")
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "bookingData.shipmentTransports", target = "transports")
  @Mapping(source = "bookingData.shipmentCutOffTimes", target = "shipmentCutOffTimes")
  @Mapping(source = "bookingData.shipmentLocations", target = "shipmentLocations")
  @Mapping(source = "bookingData.confirmedEquipments", target = "confirmedEquipments")
  @Mapping(source = "bookingData.advanceManifestFilings", target = "advanceManifestFilings")
  @Mapping(source = "bookingData.charges", target = "charges")
  @Mapping(source = "bookingData.carrierClauses", target = "carrierClauses")
  // TODO: Remove later (in DT-389)
  @Mapping(source = "bookingRequestCreatedDateTime", target = "shipmentCreatedDateTime")
  @Mapping(source = "bookingRequestUpdatedDateTime", target = "shipmentUpdatedDateTime")
  ConfirmedBookingTO toConfirmedDTO(Booking booking);

  BookingRequestRefStatusTO toStatusDTO(Booking booking);

  ConfirmedBookingRefStatusTO toConfirmedStatusDTO(Booking booking);
}

