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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommodityServiceTest {
  @Mock private RequestedEquipmentGroupService requestedEquipmentGroupService;
  @Mock private CommodityRepository commodityRepository;
  @Spy private CommodityMapper commodityMapper = Mappers.getMapper(CommodityMapper.class);

  @InjectMocks private CommodityService commodityService;

  @BeforeEach
  public void resetMocks() {
    reset(requestedEquipmentGroupService, commodityRepository);
  }

  @Test
  public void testCreateNull() {
    commodityService.createCommodities(null, null);

    verify(commodityRepository, never()).save(any());
    verify(commodityMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreateEmpty() {
    commodityService.createCommodities(Collections.emptyList(), null);

    verify(commodityMapper, never()).toDAO(any(), any());
    verify(commodityRepository, never()).save(any());
  }

  @Test
  public void testCreate() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    CommodityTO commodityTO = CommodityDataFactory.singleCommodityTO();
    Commodity commodity = CommodityDataFactory.singleCommodityWithoutId();
    when(commodityRepository.save(commodity)).thenReturn(commodity);

    // Execute
    commodityService.createCommodities(List.of(commodityTO), booking);

    // Verify
    verify(commodityRepository).save(commodity);
    verify(requestedEquipmentGroupService).createRequestedEquipments(commodityTO.requestedEquipments(), booking, commodity);
  }
}
