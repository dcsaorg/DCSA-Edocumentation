package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.repository.PartyContactDetailsRepository;
import org.dcsa.edocumentation.domain.persistence.repository.PartyIdentifyingCodeRepository;
import org.dcsa.edocumentation.domain.persistence.repository.PartyRepository;
import org.dcsa.edocumentation.service.mapping.PartyMapper;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.edocumentation.transferobjects.TaxAndLegalReferenceTO;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PartyService {

  private final PartyRepository partyRepository;
  private final PartyMapper partyMapper;
  private final PartyContactDetailsRepository partyContactDetailsRepository;
  private final TaxAndLegalReferenceRepository taxAndLegalReferenceRepository;
  private final PartyIdentifyingCodeRepository partyIdentifyingCodeRepository;

  @Transactional
  public Party createParty(PartyTO partyTO) {
    Party party =
      partyRepository.save(partyMapper.toDAO(partyTO));

    List<PartyContactDetailsTO> partyContactDetails = partyTO.partyContactDetails();
    if (partyContactDetails != null && !partyContactDetails.isEmpty()) {
      partyContactDetailsRepository.saveAll(
        partyContactDetails.stream()
          .map(partyContactDetailsTO -> partyMapper.toDAO(partyContactDetailsTO, party))
          .toList());
    }

    List<TaxAndLegalReferenceTO> taxAndLegalReferences = partyTO.taxAndLegalpartyContactDetails();
    if (partyContactDetails != null && !partyContactDetails.isEmpty()) {
      partyContactDetailsRepository.saveAll(
        partyContactDetails.stream()
          .map(partyContactDetailsTO -> partyMapper.toDAO(partyContactDetailsTO, party))
          .toList());
    }

    List<PartyIdentifyingCodeTO> identifyingCodes = partyTO.identifyingCodes();
    if (identifyingCodes != null && !identifyingCodes.isEmpty()) {
      partyIdentifyingCodeRepository.saveAll(
        identifyingCodes.stream()
          .map(
            partyIdentifyingCodeTO ->
              partyMapper.toDAO(partyIdentifyingCodeTO, party))
          .toList());
    }

    return party;
  }
}
