package org.dcsa.edocumentation.controller;

import org.dcsa.edocumentation.service.BookingRequestService;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.dcsa.skernel.errors.infrastructure.ConcreteRequestErrorMessageExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.FallbackExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.JakartaValidationExceptionHandler;
import org.dcsa.skernel.errors.infrastructure.SpringExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = {BookingRequestController.class})
@Import({
  SpringExceptionHandler.class,
  JakartaValidationExceptionHandler.class,
  FallbackExceptionHandler.class,
  ConcreteRequestErrorMessageExceptionHandler.class,
})
class BookingRequestControllerTest {
  @Autowired MockMvc mockMvc;
  @MockBean
  BookingRequestService bookingRequestService;
  private final String path = "/bkg/v2/booking-requests";

  @Test
  void testBookingController_getBookingSingleResult() throws Exception {

    BookingRequestTO mockBookingRequestTO = BookingRequestTO.builder().carrierBookingRequestReference("1234").build();
    when(bookingRequestService.getBookingRequest("1234")).thenReturn(Optional.of(mockBookingRequestTO));

    mockMvc
        .perform(get(path + "/1234").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.carrierBookingRequestReference").exists());
  }

  @Test
  void testBookingController_getBookingInvalidCarrierBookingRequestReference() throws Exception {
    String invalidCBR = "invalid".repeat(15);
    mockMvc
        .perform(get(path + "/" + invalidCBR).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpMethod").value("GET"))
        .andExpect(jsonPath("$.errors[0].reason").value("invalidInput"))
        .andExpect(
            jsonPath("$.errors[0].message")
                .value(
                    containsString(
                        "getBookingRequest.carrierBookingRequestReference size must be between 0 and 100")));
  }

  @Test
  void testBookingController_getBookingNotFound() throws Exception {
    when(bookingRequestService.getBookingRequest(any())).thenReturn(Optional.empty());
    mockMvc
        .perform(get(path + "/IdoNotExist").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}
