package org.dcsa.edocumentation.controller;

import org.dcsa.edocumentation.service.ShippingInstructionService;
import org.dcsa.edocumentation.transferobjects.*;
import org.dcsa.edocumentation.transferobjects.enums.*;
import org.dcsa.skernel.infrastructure.transferobject.AddressTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

// Todo this test should be removed or rewritten to use the controller when implemented when this PR moves beyond draft. Kept here as a convenience for manual testing with the DB.
@SpringBootTest
@Disabled
class ShippingInstructionControllerTest {

  @Autowired ShippingInstructionService shippingInstructionService;

  @Autowired private TransactionTemplate transactionTemplate;

  @Test
  void test() {

    EquipmentTO equipmentTO = EquipmentTO.builder().equipmentReference("eq_ref_01").build();

    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO = UtilizedTransportEquipmentTO.builder()
      .isShipperOwned(true)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .cargoGrossWeight(120.3)
      .equipment(equipmentTO)
      .seals(List.of(SealTO.builder()
          .sealSource(SealSourceCode.CAR)
          .sealType(SealTypeCode.BLT)
          .sealNumber("12345")
        .build()))
      .build();

    CargoItemTO cargoItemTo = CargoItemTO.builder()
      .weightUnit(WeightUnit.KGM)
      .weight(120.3)
      .numberOfPackages(1)
      .packageCode("123")
      .equipmentReference("eq_ref_01")
      .cargoLineItems(List.of(CargoLineItemTO.builder()
          .cargoLineItemID("Foo")
          .shippingMarks("bar")
        .build()))
      .build();

    ConsignmentItemTO consignmentItemTO = ConsignmentItemTO.builder()
      .weightUnit(org.dcsa.edocumentation.transferobjects.enums.WeightUnit.KGM)
      .weight(120.4)
      .hsCode("411510")
      .descriptionOfGoods("description")
      .carrierBookingReference("cbr-b83765166707812c8ff4")
      .cargoItems(List.of(cargoItemTo))
      .build();


    DocumentPartyTO documentPartyTO = DocumentPartyTO.builder()
      .isToBeNotified(false)
      .partyFunction(PartyFunction.EBL)
      .displayedAddress(List.of("foo"))
      .party(PartyTO.builder()
        .partyName("foo party")
        .address(AddressTO.builder()
          .name("foo address name")
          .build())
        .partyContactDetails(List.of(PartyContactDetailsTO.builder()
            .name("partycontact details")
          .build()))
        .build())
      .build();

    ReferenceTO referenceTO = ReferenceTO.builder()
      .type(ReferenceType.AAO)
      .value("foo reference")
      .build();

    ShippingInstructionTO siTO = ShippingInstructionTO.builder()
      .isElectronic(true)
      .isShippedOnBoardType(true)
      .isToOrder(true)
      .consignmentItems(List.of(consignmentItemTO))
      .utilizedTransportEquipments(List.of(utilizedTransportEquipmentTO))
      .documentParties(List.of(documentPartyTO))
      .references(List.of(referenceTO))
      .build();

    transactionTemplate.execute(
        status -> {
          ShippingInstructionRefStatusTO savedSi = shippingInstructionService.createShippingInstruction(siTO);
          return null;
        });
  }
}
