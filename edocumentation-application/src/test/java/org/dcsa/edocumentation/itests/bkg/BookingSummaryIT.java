package org.dcsa.edocumentation.itests.bkg;

import org.apache.http.HttpStatus;
import org.dcsa.edocumentation.itests.PostgeSqlContextAware;
import org.dcsa.edocumentation.itests.config.RestAssuredConfigurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.dcsa.edocumentation.itests.config.TestUtil.jsonSchemaValidator;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

public class BookingSummaryIT extends PostgeSqlContextAware {

  private static final String BOOKING_SUMMARIES_ENDPOINT = "/v1/booking-summaries";

  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  void getAllBookingSummaries() {
    given()
        .contentType("application/json")
        .get(BOOKING_SUMMARIES_ENDPOINT)
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("size()", greaterThanOrEqualTo(5))
        .body(jsonSchemaValidator("bookingSummary"));
  }

  @Test
  void noBookingSummary() {
    given()
        .contentType("application/json")
        .queryParam("documentStatus", "CMPL") // Does not exist in test-data
        .get(BOOKING_SUMMARIES_ENDPOINT)
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("size()", is(0));
  }

  @Test
  void filterBookingSummariesByDocumentStatus() {
    List<String> allDocumentStatusesInTestData =
        given()
            .contentType("application/json")
            .get(BOOKING_SUMMARIES_ENDPOINT)
            .body()
            .jsonPath()
            .getList("documentStatus");

    assert (!allDocumentStatusesInTestData.isEmpty());

    for (String DocumentStatus : allDocumentStatusesInTestData) {
      given()
          .contentType("application/json")
          .queryParam("documentStatus", DocumentStatus)
          .get(BOOKING_SUMMARIES_ENDPOINT)
          .then()
          .assertThat()
          .statusCode(HttpStatus.SC_OK)
          .body(
              "size()",
              greaterThanOrEqualTo(
                  1)) // At least one booking summary exists for each documentStatus
          .body(jsonSchemaValidator("bookingSummary"));
    }
  }

  @Test
  void filterByDocumentStatusError() {

    given()
        .contentType("application/json")
        .queryParam("documentStatus", "null")
        .get(BOOKING_SUMMARIES_ENDPOINT)
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(
            "size()",
            greaterThanOrEqualTo(1)) // At least one booking summary exists for each documentStatus
        .body(jsonSchemaValidator("error"));
  }
}
