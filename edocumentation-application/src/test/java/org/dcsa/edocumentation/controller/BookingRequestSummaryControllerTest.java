package org.dcsa.edocumentation.controller;

import org.dcsa.edocumentation.datafactories.BookingSummaryDataFactory;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.service.BookingSummaryService;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.skernel.errors.infrastructure.ConcreteRequestErrorMessageExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.FallbackExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.JakartaValidationExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.SpringExceptionHandler;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.junit.Ignore;
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
@WebMvcTest(controllers = {BookingSummaryController.class})
@Import({
  SpringExceptionHandler.class,
  JakartaValidationExceptionHandler.class,
  FallbackExceptionHandler.class,
  ConcreteRequestErrorMessageExceptionHandler.class,
})
class BookingRequestSummaryControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean BookingSummaryService bookingSummaryService;
  private final String path = "/bkg/v2/booking-summaries";

  @Test
  void testBookingSummaryController_getBookingSummariesSingleResult() throws Exception {
    BookingSummaryTO mockBookingSummaryTO = BookingSummaryDataFactory.singleBookingSummaryTO();
    when(bookingSummaryService.findBookingSummaries(any(), any()))
        .thenReturn(new PagedResult<>(new PageImpl<>(List.of(mockBookingSummaryTO))));

    mockMvc
        .perform(get(path).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].carrierBookingRequestReference")
                .value(mockBookingSummaryTO.carrierBookingRequestReference()));
  }

  @Test
  void testBookingSummaryController_getBookingSummariesMultipleResult() throws Exception {
    List<BookingSummaryTO> mockBookingSummaryTO =
        BookingSummaryDataFactory.multipleBookingSummaryTos();
    when(bookingSummaryService.findBookingSummaries(any(), any()))
        .thenReturn(new PagedResult<>(new PageImpl<>(mockBookingSummaryTO)));

    mockMvc
        .perform(get(path).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].carrierBookingRequestReference")
                .value(mockBookingSummaryTO.get(0).carrierBookingRequestReference()))
        .andExpect(
            jsonPath("$.[1].carrierBookingRequestReference")
                .value(mockBookingSummaryTO.get(1).carrierBookingRequestReference()));
  }

  @Test
  void testBookingSummaryController_getBookingSummariesSingleResultWithDocumentStatus()
      throws Exception {
    BookingSummaryTO mockBookingSummaryTO = BookingSummaryDataFactory.singleBookingSummaryTO();
    when(bookingSummaryService.findBookingSummaries(any(), eq(BookingStatus.RECEIVED)))
        .thenReturn(new PagedResult<>(new PageImpl<>(List.of(mockBookingSummaryTO))));

    mockMvc
        .perform(
            get(path)
                .param("bookingStatus", BookingStatus.RECEIVED)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(
            jsonPath("$.[0].carrierBookingRequestReference")
                .value(mockBookingSummaryTO.carrierBookingRequestReference()));
  }

  @Test
  void testBookingSummaryController_getBookingSummariesWithInvalidDocumentStatus()
      throws Exception {
    BookingSummaryTO mockBookingSummaryTO = BookingSummaryDataFactory.singleBookingSummaryTO();
    when(bookingSummaryService.findBookingSummaries(any(), any()))
      .thenReturn(new PagedResult<>(new PageImpl<>(List.of(mockBookingSummaryTO))));


    mockMvc
        .perform(
            get(path)
                .param("bookingStatus", "INVALID")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpMethod").value("GET"))
        .andExpect(jsonPath("$.requestUri").value(path))
        .andExpect(jsonPath("$.errors[0].reason").value("invalidInput"))
        .andExpect(
            jsonPath("$.errors[0].message")
                .value(containsString("bookingStatus Unexpected value 'INVALID', should have been one of")));
  }
}
