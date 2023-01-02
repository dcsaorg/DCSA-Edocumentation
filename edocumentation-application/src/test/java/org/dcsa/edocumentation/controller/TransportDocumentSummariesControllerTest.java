package org.dcsa.edocumentation.controller;

import org.dcsa.edocumentation.datafactories.TransportDocumentSummaryDataFactory;
import org.dcsa.edocumentation.service.TransportDocumentSummaryService;
import org.dcsa.edocumentation.transferobjects.TransportDocumentSummaryTO;
import org.dcsa.skernel.errors.infrastructure.ConcreteRequestErrorMessageExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.FallbackExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.JavaxValidationExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.SpringExceptionHandler;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.dcsa.skernel.infrastructure.pagination.Paginator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = {TransportDocumentSummariesController.class})
@Import({
  SpringExceptionHandler.class,
  JavaxValidationExceptionHandler.class,
  FallbackExceptionHandler.class,
  ConcreteRequestErrorMessageExceptionHandler.class,
  Paginator.class
})
@DisplayName("Tests for Transport Document Summaries Controller")
class TransportDocumentSummariesControllerTest {

  private static final String PATH = "/ebl/v3/transport-document-summaries";

  @Autowired private MockMvc mockMvc;
  @MockBean private TransportDocumentSummaryService transportDocumentSummaryService;

  @Captor ArgumentCaptor<TransportDocumentSummaryService.Filters> filterCaptor;

  @Test
  void getTransportDocumentSummariesShouldReturnTOsOnValidRequest() throws Exception {

    List<TransportDocumentSummaryTO> tdSummariesTOs =
        TransportDocumentSummaryDataFactory.multipleTransportDocumentSummary();

    Page<TransportDocumentSummaryTO> tdPageMock = new PageImpl<>(tdSummariesTOs);

    when(transportDocumentSummaryService.getTransportDocumentSummaries(any()))
        .thenReturn(new PagedResult<>(tdPageMock));

    mockMvc
        .perform(get(PATH).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(
            jsonPath("$.[0].transportDocumentReference")
                .value(tdSummariesTOs.get(0).transportDocumentReference()))
        .andExpect(jsonPath("$.[0].issuingParty").isNotEmpty())
        .andExpect(
            jsonPath("$.[0].carrierBookingReferences.size()")
                .value(Matchers.greaterThanOrEqualTo(1)));
  }

  @Test
  void getTransportDocumentSummariesShouldReturnResponseWithRequestedCarrierBookingReference()
      throws Exception {

    List<TransportDocumentSummaryTO> tdSummariesTOs =
        List.of(TransportDocumentSummaryDataFactory.singleTransportDocumentSummary());

    Page<TransportDocumentSummaryTO> tdPageMock = new PageImpl<>(tdSummariesTOs);

    when(transportDocumentSummaryService.getTransportDocumentSummaries(any()))
        .thenReturn(new PagedResult<>(tdPageMock));

    mockMvc
        .perform(
            get(PATH)
                .param("carrierBookingReference", "D659FDB7E33C")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(
            jsonPath("$.[0].transportDocumentReference")
                .value(tdSummariesTOs.get(0).transportDocumentReference()))
        .andExpect(jsonPath("$.[0].issuingParty").isNotEmpty())
        .andExpect(
            jsonPath("$.[0].carrierBookingReferences.size()")
                .value(Matchers.greaterThanOrEqualTo(1)))
        .andExpect(jsonPath("$.[0].carrierBookingReferences.[0]").value("D659FDB7E33C"));

    Mockito.verify(transportDocumentSummaryService).getTransportDocumentSummaries(filterCaptor.capture());
    TransportDocumentSummaryService.Filters filters = filterCaptor.getValue();

    assertEquals("D659FDB7E33C", filters.getCarrierBookingReference());
  }

  @Test
  void getTransportDocumentSummariesShouldReturnResponseWithRequestedDocumentStatus()
      throws Exception {

    List<TransportDocumentSummaryTO> tdSummariesTOs =
        List.of(TransportDocumentSummaryDataFactory.singleTransportDocumentSummary());

    Page<TransportDocumentSummaryTO> tdPageMock = new PageImpl<>(tdSummariesTOs);

    when(transportDocumentSummaryService.getTransportDocumentSummaries(any()))
        .thenReturn(new PagedResult<>(tdPageMock));

    mockMvc
        .perform(get(PATH).param("documentStatus", "APPR").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$.[0].documentStatus").value("APPR"))
        .andExpect(jsonPath("$.[0].issuingParty").isNotEmpty())
        .andExpect(
            jsonPath("$.[0].carrierBookingReferences.size()")
                .value(Matchers.greaterThanOrEqualTo(1)))
        .andExpect(
            jsonPath("$.[0].carrierBookingReferences.[0]")
                .value(tdSummariesTOs.get(0).carrierBookingReferences().get(0)));

    Mockito.verify(transportDocumentSummaryService).getTransportDocumentSummaries(filterCaptor.capture());
    TransportDocumentSummaryService.Filters filters = filterCaptor.getValue();

    assertEquals("APPR", filters.getDocumentStatus().toString());
  }
}
