package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.AddressLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.UNLocationLocationTO;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.ValueAddedServiceRequestTO;
import org.dcsa.edocumentation.transferobjects.enums.CargoMovementType;
import org.dcsa.edocumentation.transferobjects.enums.CommunicationChannelCode;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;
import org.dcsa.edocumentation.transferobjects.enums.PartyFunction;
import org.dcsa.edocumentation.transferobjects.enums.ReceiptDeliveryType;
import org.dcsa.edocumentation.transferobjects.enums.ReferenceType;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
import org.dcsa.edocumentation.transferobjects.enums.ValueAddedServiceCode;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@UtilityClass
public class BookingRequestTODataFactory {
  public static BookingTO bookingRequestTO() {
    return BookingTO.builder()
      .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
      .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
      .cargoMovementTypeAtOrigin(CargoMovementType.BB)
      .cargoMovementTypeAtDestination(CargoMovementType.BB)
      .serviceContractReference("asdas")
      .isPartialLoadAllowed(false)
      .isExportDeclarationRequired(false)
      .isImportLicenseRequired(false)
      .communicationChannelCode(CommunicationChannelCode.AO)
      .isEquipmentSubstitutionAllowed(false)
      .placeOfBLIssue(UNLocationLocationTO.builder()
        .locationName("unlocation location")
        .UNLocationCode("DEHAM")
        .build()
      )
      .invoicePayableAt(AddressLocationTO.builder()
        .locationName("address location")
        .address(AddressTO.builder()
          .name("Henrik")
          .build())
        .build()
      )
      .commodities(List.of(
        CommodityTO.builder()
          .commodityType("blah blah blah")
          .cargoGrossWeight(3.14f)
          .cargoGrossWeightUnit(WeightUnit.KGM)
        .build(),
        CommodityTO.builder()
          .commodityType("blip blip blip")
          .cargoGrossWeight(3.14f)
          .cargoGrossWeightUnit(WeightUnit.KGM)
          .build()
        )
      )
      .requestedEquipments(List.of(
        RequestedEquipmentTO.builder()
          .sizeType("22GP")
          .units(2)
          .equipmentReferences(List.of("BMOU2149612", "APZU4812090"))
          .isShipperOwned(true)
          .build()
      ))
      .references(List.of(ReferenceTO.builder()
        .type(ReferenceType.CR)
        .value("boink")
        .build())
      )
      .valueAddedServiceRequests(List.of(ValueAddedServiceRequestTO.builder()
        .valueAddedServiceCode(ValueAddedServiceCode.CDECL)
        .build())
      )
      .shipmentLocations(List.of(ShipmentLocationTO.builder()
          .displayedName("pling")
          .shipmentLocationTypeCode(ShipmentLocationTypeCode.ECP)
          .location(AddressLocationTO.builder()
            .locationName("address location")
            .address(AddressTO.builder()
              .name("Henrik")
              .build())
            .build())
          .eventDateTime(OffsetDateTime.now())
        .build()
      ))
      .documentParties(List.of(DocumentPartyTO.builder()
          .partyFunction(PartyFunction.BA)
          .displayedAddress(List.of("line1", "line2"))
          .isToBeNotified(true)
          .party(PartyTO.builder()
          .address(AddressTO.builder()
            .name("Henrik")
            .build())
          .partyName("boring party")
            .identifyingCodes(List.of(PartyIdentifyingCodeTO.builder()
              .dcsaResponsibleAgencyCode(DCSAResponsibleAgencyCode.DCSA)
              .partyCode("reponsible fun")
              .codeListName("irreponsible fun")
              .build()))
            .partyContactDetails(List.of(PartyContactDetailsTO.builder()
                .name("Henrik")
              .build()))
          .build())
        .build()))
      .expectedDepartureDate(LocalDate.now())
      .build();
  }
}
