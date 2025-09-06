package org.taskmanagementapp.project.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class SearchResourceTest {

  @Test
  public void testSearchByQuery() {
    // Create project with unique key
    String uniqueKey = "SEARCH" + System.currentTimeMillis();
    Integer projectId = given()
        .contentType(ContentType.JSON)
        .body(String.format("""
            {
              "name": "Search Project",
              "key": "%s",
              "ownerId": 1
            }
            """, uniqueKey))
        .when().post("/projects")
        .then().statusCode(201)
        .extract().path("id");

    // Create tasks
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Fix login bug",
              "description": "Login form validation issue"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then().statusCode(201);

    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Add new feature",
              "description": "Implement user dashboard"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then().statusCode(201);

    // Search by title
    given()
        .queryParam("q", "login")
        .when().get("/search")
        .then()
        .statusCode(200)
        .body("size()", equalTo(1))
        .body("[0].title", containsString("login"));

    // Search by description
    given()
        .queryParam("q", "dashboard")
        .when().get("/search")
        .then()
        .statusCode(200)
        .body("size()", equalTo(1))
        .body("[0].description", containsString("dashboard"));
  }

  @Test
  public void testSearchByProjectId() {
    // Create two projects with unique keys
    String uniqueKey1 = "PROJ1" + System.currentTimeMillis();
    String uniqueKey2 = "PROJ2" + System.currentTimeMillis();

    Integer projectId1 = given()
        .contentType(ContentType.JSON)
        .body(String.format("""
            {
              "name": "Project 1",
              "key": "%s",
              "ownerId": 1
            }
            """, uniqueKey1))
        .when().post("/projects")
        .then().statusCode(201)
        .extract().path("id");

    Integer projectId2 = given()
        .contentType(ContentType.JSON)
        .body(String.format("""
            {
              "name": "Project 2",
              "key": "%s",
              "ownerId": 1
            }
            """, uniqueKey2))
        .when().post("/projects")
        .then().statusCode(201)
        .extract().path("id");

    // Create tasks in different projects
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Task in Project 1"
            }
            """)
        .when().post("/projects/" + projectId1 + "/tasks")
        .then().statusCode(201);

    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Task in Project 2"
            }
            """)
        .when().post("/projects/" + projectId2 + "/tasks")
        .then().statusCode(201);

    // Search by project ID
    given()
        .queryParam("projectId", projectId1)
        .when().get("/search")
        .then()
        .statusCode(200)
        .body("size()", equalTo(1))
        .body("[0].projectId", equalTo(projectId1));
  }

  @Test
  public void testSearchByAssigneeId() {
    // Create project with unique key
    String uniqueKey = "ASSIGN" + System.currentTimeMillis();
    Integer projectId = given()
        .contentType(ContentType.JSON)
        .body(String.format("""
            {
              "name": "Assignee Project",
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
              "title": "Assigned Task"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then().statusCode(201)
        .extract().path("id");

    // Assign task
    given()
        .when().post("/tasks/" + taskId + "/assign/123")
        .then().statusCode(200);

    // Search by assignee ID
    given()
        .queryParam("assigneeId", 123)
        .when().get("/search")
        .then()
        .statusCode(200)
        .body("size()", equalTo(1))
        .body("[0].assigneeId", equalTo(123));
  }

  @Test
  public void testSearchWithNoResults() {
    given()
        .queryParam("q", "nonexistent")
        .when().get("/search")
        .then()
        .statusCode(200)
        .body("size()", equalTo(0));
  }
}