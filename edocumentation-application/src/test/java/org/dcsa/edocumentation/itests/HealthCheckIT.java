package org.dcsa.edocumentation.itests;

import io.restassured.http.ContentType;
import org.dcsa.edocumentation.itests.config.RestAssuredConfigurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HealthCheckIT extends PostgeSqlContextAware {
  @BeforeAll
  public static void initializeRestAssured() {
    RestAssuredConfigurator.initialize();
  }

  @Test
  void testHealth() {
    given()
      .contentType("application/json")
      .get("/actuator/health")
      .then()
      .assertThat()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("status", equalTo("UP"))
    ;
  }
}
