package org.taskmanagementapp.project.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TaskResourceTest {

  @Test
  void testCreateAndGetTasks() {
    // Create project first
    var projectResponse = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Task Test Project",
              "key": "TASKTEST",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(201)
        .extract().response();

    Long projectId = projectResponse.jsonPath().getLong("id");

    // Create task
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Test Task",
              "description": "Test Description",
              "status": "TODO"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("title", is("Test Task"))
        .body("projectId", is(projectId.intValue()));

    // Get tasks for project
    given()
        .when().get("/projects/" + projectId + "/tasks")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON);
  }

  @Test
  void testCreateTaskMissingTitle() {
    // Create project first
    var projectResponse = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Task Test Project 2",
              "key": "TASKTEST2",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(201)
        .extract().response();

    Long projectId = projectResponse.jsonPath().getLong("id");

    // Try to create task without title
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "description": "Test Description"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then()
        .statusCode(400)
        .body(is("Task title is required"));
  }

  @Test
  void testCreateTaskProjectNotFound() {
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Test Task",
              "description": "Test Description"
            }
            """)
        .when().post("/projects/999/tasks")
        .then()
        .statusCode(404)
        .body(is("Project not found"));
  }

  @Test
  void testTaskDirectOperations() {
    // Create project and task first
    var projectResponse = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "name": "Direct Task Test Project",
              "key": "DIRECTTEST",
              "ownerId": 1
            }
            """)
        .when().post("/projects")
        .then()
        .statusCode(201)
        .extract().response();

    Long projectId = projectResponse.jsonPath().getLong("id");

    var taskResponse = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Direct Test Task",
              "description": "Direct Test Description"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then()
        .statusCode(201)
        .extract().response();

    Long taskId = taskResponse.jsonPath().getLong("id");

    // Get task by ID
    given()
        .when().get("/tasks/" + taskId)
        .then()
        .statusCode(200)
        .body("title", is("Direct Test Task"));

    // Update task with PATCH
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Updated Task Title",
              "status": "IN_PROGRESS"
            }
            """)
        .when().patch("/tasks/" + taskId)
        .then()
        .statusCode(200)
        .body("title", is("Updated Task Title"));

    // Assign task to user
    given()
        .when().post("/tasks/" + taskId + "/assign/2")
        .then()
        .statusCode(200)
        .body("assigneeId", is(2));

    // Delete task
    given()
        .when().delete("/tasks/" + taskId)
        .then()
        .statusCode(204);

    // Verify task is deleted
    given()
        .when().get("/tasks/" + taskId)
        .then()
        .statusCode(404);
  }

  @Test
  void testTaskAssignmentNotFound() {
    given()
        .when().post("/tasks/999/assign/1")
        .then()
        .statusCode(404);
  }

  @Test
  void testTaskUpdateNotFound() {
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Updated Title"
            }
            """)
        .when().patch("/tasks/999")
        .then()
        .statusCode(404);
  }
}