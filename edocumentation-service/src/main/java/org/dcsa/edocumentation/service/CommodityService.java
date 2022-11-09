package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRepository;
import org.dcsa.edocumentation.service.mapping.CommodityMapper;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CommodityService {
  private final CommodityRepository commodityRepository;
  private final CommodityMapper commodityMapper;

  public void createCommodities(Collection<CommodityTO> commodities, Booking booking) {
    if (commodities != null && !commodities.isEmpty()) {
      commodityRepository.saveAll(
        commodities.stream()
          .map(commodityTO -> commodityMapper.toDAO(commodityTO, booking))
          .toList()
      );
    }
  }
}
