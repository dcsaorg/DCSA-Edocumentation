package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.DisplayedAddress;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PartyFunction;

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
}
