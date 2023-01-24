package org.dcsa.edocumentation.controller;

import org.dcsa.edocumentation.service.BookingService;
import org.dcsa.edocumentation.transferobjects.BookingTO;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = {BookingController.class})
@Import({
  SpringExceptionHandler.class,
  JakartaValidationExceptionHandler.class,
  FallbackExceptionHandler.class,
  ConcreteRequestErrorMessageExceptionHandler.class,
})
class BookingControllerTest {
  @Autowired MockMvc mockMvc;
  @MockBean BookingService bookingService;
  private final String path = "/bkg/v2/bookings";

  @Test
  void testBookingController_getBookingSingleResult() throws Exception {

    BookingTO mockBookingTO = BookingTO.builder().carrierBookingRequestReference("1234").build();
    when(bookingService.getBooking("1234")).thenReturn(mockBookingTO);

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
                        "getBooking.carrierBookingRequestReference size must be between 0 and 100")));
  }
}
