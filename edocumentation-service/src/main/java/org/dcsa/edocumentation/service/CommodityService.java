package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.CommodityRequestedEquipmentLink;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRepository;
import org.dcsa.edocumentation.service.mapping.CommodityMapper;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommodityService {
  private final CommodityRepository commodityRepository;
  private final CommodityMapper commodityMapper;

  @Transactional(TxType.MANDATORY)
  public void createCommodities(
    Collection<CommodityTO> commodities,
    Booking booking,
    Map<String, CommodityRequestedEquipmentLink> commodityRequestedEquipmentLinkMap
  ) {
    if (commodities != null && !commodities.isEmpty()) {
      commodityRepository.saveAll(
        commodities.stream()
          .map(commodityTO -> commodityMapper.toDAO(commodityTO, booking, commodityRequestedEquipmentLinkMap))
          .toList()
      );
    }
  }
}
