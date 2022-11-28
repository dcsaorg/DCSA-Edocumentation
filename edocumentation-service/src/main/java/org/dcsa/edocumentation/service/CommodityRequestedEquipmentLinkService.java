package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.CommodityRequestedEquipmentLink;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRequestedEquipmentLinkRepository;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommodityRequestedEquipmentLinkService {
  private final CommodityRequestedEquipmentLinkRepository commodityRequestedEquipmentLinkRepository;

  @Transactional
  public Map<String, CommodityRequestedEquipmentLink> createCommodityRequestedEquipmentLinks(BookingTO bookingRequest) {
    // Gather them together in a set to ensure they are unique
    Set<String> requestedEquipmentLinks = extractLinks(bookingRequest.requestedEquipments(), RequestedEquipmentTO::commodityRequestedEquipmentLink);
    Set<String> commodityLinks = extractLinks(bookingRequest.commodities(), CommodityTO::commodityRequestedEquipmentLink);

    if (!requestedEquipmentLinks.isEmpty() || !commodityLinks.isEmpty()) {
      if (!requestedEquipmentLinks.equals(commodityLinks)) {
        throw ConcreteRequestErrorMessageException.invalidInput(
          "commodityRequestedEquipmentLink in requestedEquipments " + requestedEquipmentLinks
            + " and commodities " + commodityLinks + " does not match");
      }
      return commodityRequestedEquipmentLinkRepository.saveAll(
        requestedEquipmentLinks.stream().map(link ->
          CommodityRequestedEquipmentLink.builder().commodityRequestedEquipmentLink(link).build()
        ).toList()
        ).stream()
        .collect(Collectors.toMap(CommodityRequestedEquipmentLink::getCommodityRequestedEquipmentLink, Function.identity()));
    }

    return Collections.emptyMap();
  }

  private <T> Set<String> extractLinks(List<T> list, Function<T, String> extractLink) {
    if (list != null && !list.isEmpty()) {
      return list.stream().map(extractLink).filter(Objects::nonNull).collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }
}
