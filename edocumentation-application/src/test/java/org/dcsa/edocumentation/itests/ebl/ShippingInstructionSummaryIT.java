package org.dcsa.edocumentation.itests.ebl;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.dcsa.edocumentation.itests.PostgeSqlContextAware;
import org.dcsa.edocumentation.itests.config.RestAssuredConfigurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.dcsa.edocumentation.itests.config.TestUtil.jsonSchemaValidator;
import static org.hamcrest.Matchers.*;

class ShippingInstructionSummaryIT extends PostgeSqlContextAware {

  private static final String SHIPPING_INSTRUCTIONS_SUMMARIES_ENDPOINT = "/ebl/v2/shipping-instructions-summaries";

  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  void allShippingInstructions() {
    given()
      .contentType("application/json")
      .get(SHIPPING_INSTRUCTIONS_SUMMARIES_ENDPOINT)
      .then()
      .assertThat()
      .statusCode(HttpStatus.SC_OK)
      .body("size()", greaterThanOrEqualTo(5)) // We know that the test data set contains at least 5 shipping instructions
      .body(jsonSchemaValidator("shippingInstructionSummary"));
  }

  @Test
  void noShippingInstructions() {
    given()
      .contentType("application/json")
      .queryParam("carrierBookingReference", "do_not_exist")
      .get(SHIPPING_INSTRUCTIONS_SUMMARIES_ENDPOINT)
      .then()
      .assertThat()
      .statusCode(HttpStatus.SC_OK)
      .body("size()", is(0));
  }

  @Test
  void filterByDocumentStatusNoError() {
    Response allResponse =
      given()
        .contentType("application/json")
        .get(SHIPPING_INSTRUCTIONS_SUMMARIES_ENDPOINT);
    int allCount = allResponse.body().jsonPath().getList("$").size();

    given()
      .contentType("application/json")
      .queryParam("documentStatus", "RECE")
      .get(SHIPPING_INSTRUCTIONS_SUMMARIES_ENDPOINT)
      .then()
      .assertThat()
      .statusCode(HttpStatus.SC_OK)
      .body("size()", greaterThanOrEqualTo(3)) // We know that the test data set contains at least 3 shipping instructions with RECE
      .body("size()", lessThan(allCount))
      .body(jsonSchemaValidator("shippingInstructionSummary"));
  }

  @Test
  void combineCarrierBookingReferencesAndDocumentStatusNoError() {
    given()
      .contentType("application/json")
      .queryParam("carrierBookingReference", "bca68f1d3b804ff88aaa1e43055432f7")
      .queryParam("documentStatus", "RECE")
      .get(SHIPPING_INSTRUCTIONS_SUMMARIES_ENDPOINT)
      .then()
      .assertThat()
      .statusCode(HttpStatus.SC_OK)
      .body(jsonSchemaValidator("shippingInstructionSummary"));
  }
}
