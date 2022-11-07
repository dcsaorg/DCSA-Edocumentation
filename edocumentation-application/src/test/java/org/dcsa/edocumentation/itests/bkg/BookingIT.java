package org.dcsa.edocumentation.itests.bkg;

import io.restassured.http.ContentType;
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
      .body(BookingRequestTODataFactory.bookingRequestTO())
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
}
