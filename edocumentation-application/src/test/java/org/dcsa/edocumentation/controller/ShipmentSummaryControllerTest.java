package org.dcsa.edocumentation.controller;

import org.dcsa.edocumentation.datafactories.ShipmentSummaryDataFactory;
import org.dcsa.edocumentation.service.ShipmentSummaryService;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.infrastructure.ConcreteRequestErrorMessageExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.FallbackExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.JavaxValidationExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.SpringExceptionHandler;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = {ShipmentSummaryController.class})
@Import({
  SpringExceptionHandler.class,
  JavaxValidationExceptionHandler.class,
  FallbackExceptionHandler.class,
  ConcreteRequestErrorMessageExceptionHandler.class
})
class ShipmentSummaryControllerTest {
  private final String path = "/bkg/v2/shipment-summaries";
  @Autowired MockMvc mockMvc;
  @MockBean ShipmentSummaryService shipmentSummaryService;

  @Test
  void testShipmentSummaryController_getShipmentSummariesSingleResult() throws Exception {
    ShipmentSummaryTO mockShipmentSummaryTO = ShipmentSummaryDataFactory.singleShipmentSummaryTO();
    when(shipmentSummaryService.findShipmentSummaries(any(), any()))
        .thenReturn(new PagedResult<>(new PageImpl<>(List.of(mockShipmentSummaryTO))));

    mockMvc
        .perform(get(path).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].carrierBookingReference")
                .value(mockShipmentSummaryTO.carrierBookingReference()));
  }

  @Test
  void testShipmentSummaryController_getShipmentSummariesSingleResulWithDocumentStatus()
      throws Exception {
    ShipmentSummaryTO mockShipmentSummaryTO = ShipmentSummaryDataFactory.singleShipmentSummaryTO();
    when(shipmentSummaryService.findShipmentSummaries(any(), eq(BkgDocumentStatus.RECE)))
        .thenReturn(new PagedResult<>(new PageImpl<>(List.of(mockShipmentSummaryTO))));

    mockMvc
        .perform(get(path).param("documentStatus", "RECE").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].carrierBookingReference")
                .value(mockShipmentSummaryTO.carrierBookingReference()))
        .andExpect(
            jsonPath("$.[0].documentStatus").value(mockShipmentSummaryTO.documentStatus().name()));
    ;
  }

  @Test
  void testShipmentSummaryController_getShipmentSummariesMultipleResult() throws Exception {
    List<ShipmentSummaryTO> mockShipmentSummaryTO =
        ShipmentSummaryDataFactory.multipleShipmentSummaryTO();
    when(shipmentSummaryService.findShipmentSummaries(any(), any()))
        .thenReturn(new PagedResult<>(new PageImpl<>(mockShipmentSummaryTO)));

    mockMvc
        .perform(get(path).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].carrierBookingReference")
                .value(mockShipmentSummaryTO.get(0).carrierBookingReference()))
        .andExpect(
            jsonPath("$.[1].carrierBookingReference")
                .value(mockShipmentSummaryTO.get(1).carrierBookingReference()));
    ;
  }

  @Test
  void testShipmentSummaryController_getShipmentSummariesWithInvalidDocumentStatus()
      throws Exception {

    mockMvc
        .perform(get(path).param("documentStatus", "INVALID").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpMethod").value("GET"))
        .andExpect(jsonPath("$.requestUri").value(path))
        .andExpect(jsonPath("$.errors[0].reason").value("invalidParameter"))
        .andExpect(
            jsonPath("$.errors[0].message")
                .value(containsString("'documentStatus' must be of type BkgDocumentStatus")));
  }
}
