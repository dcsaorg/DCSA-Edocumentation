package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.domain.persistence.entity.CommodityRequestedEquipmentLink;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRequestedEquipmentLinkRepository;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.dcsa.skernel.errors.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommodityRequestedEquipmentLinkServiceTest {
  @Mock private CommodityRequestedEquipmentLinkRepository commodityRequestedEquipmentLinkRepository;

  @InjectMocks private CommodityRequestedEquipmentLinkService commodityRequestedEquipmentLinkService;

  @BeforeEach
  public void resetMocks() {
    reset(commodityRequestedEquipmentLinkRepository);
  }

  @Test
  public void testNull() {
    // Setup
    BookingTO bookingRequest = BookingTO.builder()
      .build();

    // Execute
    Map<String, CommodityRequestedEquipmentLink> actualResult =
      commodityRequestedEquipmentLinkService.createCommodityRequestedEquipmentLinks(bookingRequest);

    // Verify
    assertTrue(actualResult.isEmpty());
    verify(commodityRequestedEquipmentLinkRepository, never()).saveAll(any());
  }

  @Test
  public void testEmpty() {
    // Setup
    BookingTO bookingRequest = BookingTO.builder()
      .requestedEquipments(requestedEquipmentTOs(null, null))
      .commodities(commodityTOs(null, null))
      .build();

    // Execute
    Map<String, CommodityRequestedEquipmentLink> actualResult =
      commodityRequestedEquipmentLinkService.createCommodityRequestedEquipmentLinks(bookingRequest);

    // Verify
    assertTrue(actualResult.isEmpty());
    verify(commodityRequestedEquipmentLinkRepository, never()).saveAll(any());
  }

  @Test
  public void testMismatch() {
    // Setup
    BookingTO bookingRequest = BookingTO.builder()
      .requestedEquipments(requestedEquipmentTOs("rtg"))
      .commodities(commodityTOs("qwer"))
      .build();

    // Execute
    BadRequestException exception = assertThrows(BadRequestException.class, () ->
      commodityRequestedEquipmentLinkService.createCommodityRequestedEquipmentLinks(bookingRequest));

    // Verify
    assertEquals("commodityRequestedEquipmentLink in requestedEquipments [rtg] and commodities [qwer] does not match", exception.getMessage());
    verify(commodityRequestedEquipmentLinkRepository, never()).saveAll(any());
  }

  @Test
  public void testSaveUnique() {
    // Setup
    BookingTO bookingRequest = BookingTO.builder()
      .requestedEquipments(requestedEquipmentTOs("rtg", "rtg", "asdf"))
      .commodities(commodityTOs("asdf", "asdf", "rtg"))
      .build();
    when(commodityRequestedEquipmentLinkRepository.saveAll(any()))
      .thenReturn(List.of(
        CommodityRequestedEquipmentLink.builder().commodityRequestedEquipmentLink("rtg").build(),
        CommodityRequestedEquipmentLink.builder().commodityRequestedEquipmentLink("asdf").build()
      ));

    // Execute
    Map<String, CommodityRequestedEquipmentLink> actualResult =
      commodityRequestedEquipmentLinkService.createCommodityRequestedEquipmentLinks(bookingRequest);

    // Verify
    ArgumentCaptor<List<CommodityRequestedEquipmentLink>> linkCaptor = ArgumentCaptor.forClass(List.class);
    assertEquals(Set.of("rtg", "asdf"), actualResult.keySet());
    verify(commodityRequestedEquipmentLinkRepository).saveAll(linkCaptor.capture());
    assertEquals(2, linkCaptor.getValue().size());
  }

  private List<RequestedEquipmentTO> requestedEquipmentTOs(String... links) {
    return Arrays.stream(links)
      .map(link -> RequestedEquipmentTO.builder().commodityRequestedEquipmentLink(link).build())
      .toList();
  }

  private List<CommodityTO> commodityTOs(String... links) {
    return Arrays.stream(links)
      .map(link -> CommodityTO.builder().commodityRequestedEquipmentLink(link).build())
      .toList();
  }
}
