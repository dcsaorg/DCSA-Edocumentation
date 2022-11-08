package org.dcsa.edocumentation.itests.bkg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.dcsa.edocumentation.datafactories.BookingRequestTODataFactory;
import org.dcsa.edocumentation.itests.PostgeSqlContextAware;
import org.dcsa.edocumentation.itests.config.RestAssuredConfigurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class BookingIT extends PostgeSqlContextAware {
  private static final String BOOKINGS_ENDPOINT = "/bkg/v1/bookings";

  @Autowired ObjectMapper mapper;

  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  void testCreate() throws JsonProcessingException {

    given()
        .contentType("application/json")
        .body(mapper.writeValueAsString(BookingRequestTODataFactory.bookingRequestTO()))
        .post(BOOKINGS_ENDPOINT)
        .then()
        .assertThat()
        .statusCode(HttpStatus.CREATED.value())
     .contentType(ContentType.JSON)
//     .extract()
//     .body()
//      .asString()
    // .jsonPath()
    // .getObject(".", BookingTO.class)
    ;

  }
}
