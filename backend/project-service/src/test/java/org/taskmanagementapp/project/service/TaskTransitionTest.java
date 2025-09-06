package org.taskmanagementapp.project.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TaskTransitionTest {

  @Test
  public void testTransitionTask() {
    // Create project first with unique key
    String uniqueKey = "TEST" + System.currentTimeMillis();
    Integer projectId = given()
        .contentType(ContentType.JSON)
        .body(String.format("""
            {
              "name": "Test Project",
              "key": "%s",
              "ownerId": 1
            }
            """, uniqueKey))
        .when().post("/projects")
        .then().statusCode(201)
        .extract().path("id");

    // Create task
    Integer taskId = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Test Task",
              "status": "TODO"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then().statusCode(201)
        .extract().path("id");

    // Transition task to IN_PROGRESS
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "status": "IN_PROGRESS"
            }
            """)
        .when().post("/tasks/" + taskId + "/transition")
        .then()
        .statusCode(200)
        .body("status", equalTo("IN_PROGRESS"));
  }

  @Test
  public void testTransitionNonExistentTask() {
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "status": "DONE"
            }
            """)
        .when().post("/tasks/999/transition")
        .then()
        .statusCode(404);
  }
}