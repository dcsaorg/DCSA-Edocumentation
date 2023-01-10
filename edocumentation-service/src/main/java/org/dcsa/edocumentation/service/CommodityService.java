package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRepository;
import org.dcsa.edocumentation.service.mapping.CommodityMapper;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CommodityService {
  private final RequestedEquipmentGroupService requestedEquipmentGroupService;
  private final CommodityRepository commodityRepository;
  private final CommodityMapper commodityMapper;

  @Transactional(TxType.MANDATORY)
  public void createCommodities(
    Collection<CommodityTO> commodities,
    Booking booking
  ) {
    if (commodities != null && !commodities.isEmpty()) {
      commodities.forEach(commodityTO -> {
        Commodity commodity = commodityRepository.save(commodityMapper.toDAO(commodityTO, booking));
        requestedEquipmentGroupService.createRequestedEquipments(commodityTO.requestedEquipments(), booking, commodity);
      });
    }
  }
}
