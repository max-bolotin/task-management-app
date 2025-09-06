package org.taskmanagementapp.project.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProjectResourceTest {

  @Test
  void testGetAllProjects() {
    given()
        .when().get("/projects")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON);
  }

  @Test
  void testCreateProject() {
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Test Project",
              "key": "TEST",
              "description": "Test Description",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("name", is("Test Project"))
        .body("key", is("TEST"));
  }

  @Test
  void testCreateProjectMissingName() {
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "key": "TEST",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(400)
        .body(is("Project name is required"));
  }

  @Test
  void testCreateProjectMissingOwner() {
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Test Project",
              "key": "TEST"
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(400)
        .body(is("Owner ID is required"));
  }

  @Test
  void testCreateProjectDuplicateKey() {
    // Create first project
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "First Project",
              "key": "DUPLICATE",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(201);

    // Try to create second project with same key
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Second Project",
              "key": "DUPLICATE",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(409)
        .body(is("Project key already exists"));
  }

  @Test
  void testGetProjectById() {
    // Create project first
    var response = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Get Test Project",
              "key": "GETTEST",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(201)
        .extract().response();

    Long projectId = response.jsonPath().getLong("id");

    // Get project by ID
    given()
        .when().get("/projects/" + projectId)
        .then()
        .statusCode(200)
        .body("name", is("Get Test Project"))
        .body("key", is("GETTEST"));
  }

  @Test
  void testGetProjectNotFound() {
    given()
        .when().get("/projects/999")
        .then()
        .statusCode(404);
  }

  @Test
  void testDeleteProject() {
    // Create project first
    var response = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Delete Test Project",
              "key": "DELTEST",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(201)
        .extract().response();

    Long projectId = response.jsonPath().getLong("id");

    // Delete project
    given()
        .when().delete("/projects/" + projectId)
        .then()
        .statusCode(204);

    // Verify project is deleted
    given()
        .when().get("/projects/" + projectId)
        .then()
        .statusCode(404);
  }
}