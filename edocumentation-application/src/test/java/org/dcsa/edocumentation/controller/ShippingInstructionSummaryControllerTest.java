package org.dcsa.edocumentation.controller;

import org.dcsa.edocumentation.datafactories.ShippingInstructionSummaryDataFactory;
import org.dcsa.edocumentation.service.ShippingInstructionSummaryService;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.skernel.errors.infrastructure.ConcreteRequestErrorMessageExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.FallbackExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.JakartaValidationExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.SpringExceptionHandler;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = {ShippingInstructionSummaryController.class})
@Import({
  SpringExceptionHandler.class,
  JakartaValidationExceptionHandler.class,
  FallbackExceptionHandler.class,
  ConcreteRequestErrorMessageExceptionHandler.class
})
class ShippingInstructionSummaryControllerTest {

  private final String path = "/ebl/v3/shipping-instructions-summaries";
  @Autowired MockMvc mockMvc;
  @MockBean ShippingInstructionSummaryService shippingInstructionSummaryService;

  @Test
  void testShippingInstructionSummary_singleResultNoDocumentStatus() throws Exception {
    ShippingInstructionSummaryTO mockShippingInstructionSummaryResponse =
        ShippingInstructionSummaryDataFactory.singleShippingInstructionSummaryTO();
    Page<ShippingInstructionSummaryTO> pageResult =
        new PageImpl<>(List.of(mockShippingInstructionSummaryResponse));
    when(shippingInstructionSummaryService.findShippingInstructionSummaries(any(), any(), any()))
        .thenReturn(new PagedResult<>(pageResult));

    mockMvc
        .perform(get(path).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].shippingInstructionReference")
                .value(mockShippingInstructionSummaryResponse.shippingInstructionReference()));
  }

  @Test
  void testShippingInstructionSummary_getShipmentSummariesSingleResulWithDocumentStatus()
      throws Exception {
    ShippingInstructionSummaryTO mockShippingInstructionSummaryResponse =
        ShippingInstructionSummaryDataFactory.singleShippingInstructionSummaryTO();
    Page<ShippingInstructionSummaryTO> pageResult =
        new PageImpl<>(List.of(mockShippingInstructionSummaryResponse));
    when(shippingInstructionSummaryService.findShippingInstructionSummaries(any(), any(), any()))
        .thenReturn(new PagedResult<>(pageResult));

    mockMvc
        .perform(get(path).param("documentStatus", "RECEIVED").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].shippingInstructionReference")
                .value(mockShippingInstructionSummaryResponse.shippingInstructionReference()))
        .andExpect(
            jsonPath("$.[0].documentStatus")
                .value(mockShippingInstructionSummaryResponse.documentStatus()));
  }

  @Test
  void testShippingInstructionSummary_getShipmentSummariesSingleResulWithCarrierBookingReference()
      throws Exception {
    ShippingInstructionSummaryTO mockShippingInstructionSummaryResponse =
        ShippingInstructionSummaryDataFactory.singleShippingInstructionSummaryTO();
    Page<ShippingInstructionSummaryTO> pageResult =
        new PageImpl<>(List.of(mockShippingInstructionSummaryResponse));
    when(shippingInstructionSummaryService.findShippingInstructionSummaries(any(), any(), any()))
        .thenReturn(new PagedResult<>(pageResult));

    mockMvc
        .perform(
            get(path)
                .param("carrierBookingReference", "bca68f1d3b804ff88aaa1e43055432f7")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].shippingInstructionReference")
                .value(mockShippingInstructionSummaryResponse.shippingInstructionReference()))
        .andExpect(
            jsonPath("$.[0].carrierBookingReferences[0]")
                .value(mockShippingInstructionSummaryResponse.carrierBookingReferences().get(0)));
  }

  @Test
  void
      testShippingInstructionSummary_getShipmentSummariesSingleResulWithCarrierBookingReferenceAndDocumentStatus()
          throws Exception {
    ShippingInstructionSummaryTO mockShippingInstructionSummaryResponse =
        ShippingInstructionSummaryDataFactory.singleShippingInstructionSummaryTO();
    Page<ShippingInstructionSummaryTO> pageResult =
        new PageImpl<>(List.of(mockShippingInstructionSummaryResponse));
    when(shippingInstructionSummaryService.findShippingInstructionSummaries(any(), any(), any()))
        .thenReturn(new PagedResult<>(pageResult));

    mockMvc
        .perform(
            get(path)
                .param("carrierBookingReference", "bca68f1d3b804ff88aaa1e43055432f7")
                .param("documentStatus", "RECEIVED")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].shippingInstructionReference")
                .value(mockShippingInstructionSummaryResponse.shippingInstructionReference()))
        .andExpect(
            jsonPath("$.[0].carrierBookingReferences[0]")
                .value(mockShippingInstructionSummaryResponse.carrierBookingReferences().get(0)))
        .andExpect(
            jsonPath("$.[0].documentStatus")
                .value(mockShippingInstructionSummaryResponse.documentStatus()));
  }

  @Test
  void testShippingInstructionSummary_getShipmentSummariesMultipleResult() throws Exception {
    List<ShippingInstructionSummaryTO> mockShippingInstructionSummaries = ShippingInstructionSummaryDataFactory.multipleShippingInstructionSummaryTO();
    Page<ShippingInstructionSummaryTO> pageResult = new PageImpl<>(mockShippingInstructionSummaries);
    when(shippingInstructionSummaryService.findShippingInstructionSummaries(any(), any(), any())).thenReturn(new PagedResult<>(pageResult));

    mockMvc
      .perform(
        get(path).accept(MediaType.APPLICATION_JSON)
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(
        jsonPath("$.[0].shippingInstructionReference")
          .value(mockShippingInstructionSummaries.get(0).shippingInstructionReference()))
      .andExpect(
        jsonPath("$.[1].carrierBookingReferences[0]")
          .value(mockShippingInstructionSummaries.get(1).carrierBookingReferences().get(0)))
      .andExpect(
        jsonPath("$.[1].shippingInstructionReference")
          .value(mockShippingInstructionSummaries.get(1).shippingInstructionReference()))
      .andExpect(
        jsonPath("$.[0].carrierBookingReferences[0]")
          .value(mockShippingInstructionSummaries.get(0).carrierBookingReferences().get(0)))
      .andExpect(
        jsonPath("$.[0].documentStatus")
          .value(mockShippingInstructionSummaries.get(0).documentStatus()));
  }

  @Test
  void testShippingInstructionSummary_getShipmentSummariesWithInvalidDocumentStatus()
      throws Exception {
    List<ShippingInstructionSummaryTO> mockShippingInstructionSummaries = ShippingInstructionSummaryDataFactory.multipleShippingInstructionSummaryTO();
    Page<ShippingInstructionSummaryTO> pageResult = new PageImpl<>(mockShippingInstructionSummaries);
    when(shippingInstructionSummaryService.findShippingInstructionSummaries(any(), any(), any())).thenReturn(new PagedResult<>(pageResult));

    mockMvc
      .perform(get(path).param("documentStatus", "INVALID").accept(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.httpMethod").value("GET"))
      .andExpect(jsonPath("$.requestUri").value(path))
      .andExpect(jsonPath("$.errors[0].reason").value("invalidInput"))
      .andExpect(
        jsonPath("$.errors[0].message")
          .value(containsString("documentStatus Unexpected value 'INVALID', should have been one of")));
  }
}
