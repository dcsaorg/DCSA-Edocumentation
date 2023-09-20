package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Carrier;
import org.dcsa.edocumentation.domain.persistence.entity.CarrierClause;
import org.dcsa.edocumentation.domain.persistence.entity.Charge;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PaymentTerm;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class TransportDocumentDataFactory {

  public TransportDocument singleTransportDocument() {
    return TransportDocument.builder()
      .id(UUID.fromString("9349a7e2-85cd-4f96-8229-d7fa8c5fc427"))
      .carrier(Carrier.builder().carrierName("Dummy Carrier").smdgCode("DMY").build())
      .issueDate(LocalDate.now())
      .receivedForShipmentDate(LocalDate.now())
      .shippedOnBoardDate(LocalDate.now())
      .transportDocumentReference("TR_REF_01")
      .transportDocumentCreatedDateTime(OffsetDateTime.now())
      .transportDocumentUpdatedDateTime(OffsetDateTime.now())
      .issuingParty(PartyDataFactory.singleParty())
      .placeOfIssue(LocationDataFactory.addressLocationWithId())
      .carrierClauses(Set.of(CarrierClause.builder()
        .id(UUID.randomUUID())
        .clauseContent("test clause")
        .build()))
      .charges(Set.of(Charge.builder()
        .id("chargeId")
        .paymentTermCode(PaymentTerm.PRE)
        .currencyAmount(100.0)
        .currencyCode("EUR")
        .build()))
      .shippingInstruction(
        ShippingInstructionDataFactory
          .singleShallowShippingInstructionWithPlaceOfIssueAndShipments())
      .build();
  }
}
