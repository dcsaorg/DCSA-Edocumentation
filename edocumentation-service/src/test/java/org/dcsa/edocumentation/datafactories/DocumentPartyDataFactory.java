package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.DisplayedAddress;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.entity.PartyContactDetails;
import org.dcsa.edocumentation.domain.persistence.entity.PartyIdentifyingCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PartyFunction;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class DocumentPartyDataFactory {

  public DocumentParty singleDocumentParty() {
    return DocumentParty.builder()
        .id(UUID.randomUUID())
        .party(PartyDataFactory.singleParty())
        .isToBeNotified(true)
        .partyFunction(PartyFunction.EBL)
        .displayedAddress(
            Set.of(
                DisplayedAddress.builder()
                    .addressLine("Strawingskylaan 4117 floor 6")
                    .addressLineNumber(1)
                    .build(),
                DisplayedAddress.builder()
                    .addressLine("1077 ZX Amsterdam Netherlands")
                    .addressLineNumber(2)
                    .build()))
        .build();
  }

  public DocumentPartyTO fullDocumentPartyTO() {
    return DocumentPartyTO.builder()
      .partyFunction(org.dcsa.edocumentation.transferobjects.enums.PartyFunction.BA)
      .displayedAddress(List.of("line1", "line2"))
      .isToBeNotified(true)
      .party(PartyTO.builder()
        .address(AddressDataFactory.addressTO())
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
      .build();
  }

  /**
   * DocumentParty part of a full DocumentParty.
   */
  public DocumentParty partialDocumentParty(Party party) {
    return DocumentParty.builder()
      .partyFunction(PartyFunction.BA)
      .party(party)
      .isToBeNotified(true)
      .build();
  }

  public Set<DisplayedAddress> displayedAddresses(DocumentParty documentParty) {
    return Set.of(
      DisplayedAddress.builder()
        .documentParty(documentParty)
        .addressLine("line1")
        .addressLineNumber(1)
        .build(),
      DisplayedAddress.builder()
        .documentParty(documentParty)
        .addressLine("line2")
        .addressLineNumber(2)
        .build()
    );
  }

  public Party partialParty() {
    return Party.builder()
      .address(AddressDataFactory.addressWithId())
      .partyName("boring party")
      .build();
  }

  public PartyIdentifyingCode partyIdentifyingCode(Party party) {
    return PartyIdentifyingCode.builder()
      .party(party)
      .dcsaResponsibleAgencyCode(org.dcsa.edocumentation.domain.persistence.entity.enums.DCSAResponsibleAgencyCode.DCSA)
      .partyCode("reponsible fun")
      .codeListName("irreponsible fun")
      .build();
  }

  public PartyContactDetails partyContactDetails(Party party) {
    return PartyContactDetails.builder()
      .party(party)
      .name("Henrik")
      .build();
  }
}
