package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.*;
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
        .partyFunction(PartyFunction.CN.name())
        .displayedAddress(
            DisplayedAddress.builder()
                .addressLine1("Strawingskylaan 4117 floor 6")
                .addressLine2("1077 ZX Amsterdam Netherlands")
                .build())
        .build();
  }

  public DocumentPartyTO fullDocumentPartyTO() {
    return DocumentPartyTO.builder()
        .partyFunction(org.dcsa.edocumentation.transferobjects.enums.PartyFunction.BA.name())
        .displayedAddress(List.of("line1", "line2"))
        .isToBeNotified(true)
        .party(
            PartyTO.builder()
                .address(AddressDataFactory.addressTO())
                .partyName("boring party")
                .identifyingCodes(
                    List.of(
                        PartyIdentifyingCodeTO.builder()
                            .dcsaResponsibleAgencyCode(DCSAResponsibleAgencyCode.DCSA)
                            .partyCode("reponsible fun")
                            .codeListName("irreponsible fun")
                            .build()))
                .partyContactDetails(
                    List.of(PartyContactDetailsTO.builder().name("Henrik").build()))
                .build())
        .build();
  }

  /** DocumentParty part of a full DocumentParty. */
  public DocumentParty partialDocumentParty(Party party) {
    return DocumentParty.builder()
        .partyFunction(PartyFunction.BA.name())
        .party(party)
        .isToBeNotified(true)
        .build();
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
        .dcsaResponsibleAgencyCode(
            org.dcsa.edocumentation.domain.persistence.entity.enums.DCSAResponsibleAgencyCode.DCSA)
        .partyCode("reponsible fun")
        .codeListName("irreponsible fun")
        .build();
  }

  public PartyContactDetails partyContactDetails(Party party) {
    return PartyContactDetails.builder().party(party).name("Henrik").build();
  }
}
