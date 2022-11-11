package org.dcsa.edocumentation.itests.bkg;

import io.restassured.http.ContentType;
import io.restassured.internal.mapping.Jackson2Mapper;
import org.dcsa.edocumentation.configuration.JacksonConfiguration;
import org.dcsa.edocumentation.datafactories.BookingRequestTODataFactory;
import org.dcsa.edocumentation.itests.PostgeSqlContextAware;
import org.dcsa.edocumentation.itests.config.RestAssuredConfigurator;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class BookingIT extends PostgeSqlContextAware {
  private static final String BOOKINGS_ENDPOINT = "/bkg/v1/bookings";

  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  public void testCreate() {
    BookingRefStatusTO bookingRefStatusTO = given()
      .contentType("application/json")
      .body(BookingRequestTODataFactory.bookingRequestTO(), jackson2Mapper())
      .post(BOOKINGS_ENDPOINT)
      .then()
      .assertThat()
      .statusCode(HttpStatus.CREATED.value())
      .contentType(ContentType.JSON)
      .extract()
      .body()
      .jsonPath()
      .getObject(".", BookingRefStatusTO.class)
      ;

    System.out.println(bookingRefStatusTO.toString());
  }

  private Jackson2Mapper jackson2Mapper() {
    return new Jackson2Mapper((cls, charset) -> new JacksonConfiguration().defaultObjectMapper());
  }
}
