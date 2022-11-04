package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.AddressLocationTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.dcsa.edocumentation.transferobjects.enums.CargoMovementType;
import org.dcsa.edocumentation.transferobjects.enums.CommunicationChannelCode;
import org.dcsa.edocumentation.transferobjects.enums.ReceiptDeliveryType;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

import java.time.LocalDate;
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
      .invoicePayableAt(AddressLocationTO.builder()
        .locationName("address location")
        .address(AddressTO.builder()
          .name("Henrik")
          .build())
        .build())
      .commodities(List.of(CommodityTO.builder()
          .commodityType("blah blah blah")
          .cargoGrossWeight(3.14f)
          .cargoGrossWeightUnit(WeightUnit.KGM)
        .build()))
      .requestedEquipments(List.of(RequestedEquipmentTO.builder()
        .requestedEquipmentSizeType("ASDF")
        .equipmentReferences(List.of("as", "123456789012345"))
        .requestedEquipmentUnits(5)
        .isShipperOwned(false)
        .build()))
      .expectedDepartureDate(LocalDate.now())
      .build();
  }
}