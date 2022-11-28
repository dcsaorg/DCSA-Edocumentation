package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.CommodityDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRepository;
import org.dcsa.edocumentation.service.mapping.CommodityMapper;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommodityServiceTest {
  @Mock private CommodityRepository commodityRepository;
  @Spy private CommodityMapper commodityMapper = Mappers.getMapper(CommodityMapper.class);

  @InjectMocks private CommodityService commodityService;

  @BeforeEach
  public void resetMocks() {
    reset(commodityRepository);
  }

  @Test
  public void testCreateNull() {
    commodityService.createCommodities(null, null, null);

    verify(commodityRepository, never()).saveAll(any());
    verify(commodityMapper, never()).toDAO(any(), any(), any());
  }

  @Test
  public void testCreateEmpty() {
    commodityService.createCommodities(Collections.emptyList(), null, null);

    verify(commodityMapper, never()).toDAO(any(), any(), any());
    verify(commodityRepository, never()).saveAll(any());
  }

  @Test
  public void testCreate() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    CommodityTO commodityTO = CommodityDataFactory.singleCommodityTO();
    Commodity commodity = CommodityDataFactory.singleCommodityWithoutId();

    // Execute
    commodityService.createCommodities(List.of(commodityTO), booking, Collections.emptyMap());

    // Verify
    verify(commodityRepository).saveAll(List.of(commodity));
  }
}
