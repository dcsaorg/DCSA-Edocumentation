package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.CarrierCodeListProvider;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.skernel.infrastructure.transferobject.AddressTO;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TransportDocumentSummaryDataFactory {

  public TransportDocumentSummaryTO singleTransportDocumentSummary() {
    return TransportDocumentSummaryTO.builder()
        .transportDocumentReference(UUID.randomUUID().toString())
        .shippingInstructionReference(UUID.randomUUID().toString())
        .documentStatus(EblDocumentStatus.APPR)
        .transportDocumentCreatedDateTime(OffsetDateTime.now())
        .transportDocumentUpdatedDateTime(OffsetDateTime.now())
        .issueDate(LocalDate.now())
        .shippedOnBoardDate(LocalDate.now())
        .receivedForShipmentDate(LocalDate.now())
        .numberOfOriginalsWithCharges(2)
        .numberOfOriginalsWithoutCharges(3)
        .carrierCode("MSC")
        .carrierCodeListProvider(CarrierCodeListProvider.SMDG)
        .issuingParty(issuerParty())
        .numberOfRiderPages(4)
        .carrierBookingReferences(List.of("D659FDB7E33C"))
        .build();
  }

  public List<TransportDocumentSummaryTO> multipleTransportDocumentSummary() {
    TransportDocumentSummaryTO transportDocumentSummaryTO =
        TransportDocumentSummaryTO.builder()
            .transportDocumentReference(UUID.randomUUID().toString())
            .shippingInstructionReference(UUID.randomUUID().toString())
            .documentStatus(EblDocumentStatus.PENU)
            .transportDocumentCreatedDateTime(OffsetDateTime.now())
            .transportDocumentUpdatedDateTime(OffsetDateTime.now())
            .issueDate(LocalDate.now())
            .shippedOnBoardDate(LocalDate.now())
            .receivedForShipmentDate(LocalDate.now())
            .numberOfOriginalsWithCharges(3)
            .numberOfOriginalsWithoutCharges(4)
            .carrierCode("CMA")
            .carrierCodeListProvider(CarrierCodeListProvider.SMDG)
            .issuingParty(issuerParty())
            .numberOfRiderPages(5)
            .carrierBookingReferences(List.of("E379021B7782", "E379021B7782", "A379021B7782"))
            .build();

    return List.of(singleTransportDocumentSummary(), transportDocumentSummaryTO);
  }

  private PartyTO issuerParty() {
    return PartyTO.builder()
        .partyName("FTL International")
        .taxReference1("ref 1")
        .taxReference2("ref 2")
        .publicKey(UUID.randomUUID().toString())
        .address(address())
        .partyContactDetails(List.of(partyContactDetails()))
        .identifyingCodes(
            List.of(
                PartyIdentifyingCodeTO.builder()
                    .partyCode("ABC")
                    .dcsaResponsibleAgencyCode(DCSAResponsibleAgencyCode.DCSA)
                    .codeListName("DCSA")
                    .build()))
        .build();
  }

  private PartyContactDetailsTO partyContactDetails() {
    return PartyContactDetailsTO.builder()
        .name("DCSA")
        .phone("31123456789")
        .email("info@dcsa.org")
        .url("https://www.dcsa.org")
        .build();
  }

  private AddressTO address() {
    return AddressTO.builder()
        .name("Lukas")
        .street("Rohrdamm")
        .streetNumber("81")
        .floor("5")
        .postCode("32108")
        .city("Bad Salzuflen Grastrup-hölsen")
        .stateRegion("Nordrhein-Westfalen")
        .country("Germany")
        .build();
  }
}
