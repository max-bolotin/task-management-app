package org.taskmanagementapp.project.service;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.taskmanagementapp.project.BaseAuthenticatedTest;

@QuarkusTest
public class SimpleSearchTest extends BaseAuthenticatedTest {

  @Test
  public void testSearchEndpointExists() {
    given()
        .header("Authorization", "Bearer " + VALID_JWT)
        .when().get("/search")
        .then()
        .statusCode(200);
  }
}
