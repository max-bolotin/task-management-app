package org.taskmanagementapp.project.service;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class SimpleSearchTest {

  @Test
  public void testSearchEndpointExists() {
    given()
        .when().get("/search")
        .then()
        .statusCode(200);
  }
}