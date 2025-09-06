package org.taskmanagementapp.project.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CommentResourceTest {

  @Test
  public void testCreateAndGetComments() {
    // Create project with unique key
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
              "title": "Test Task"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then().statusCode(201)
        .extract().path("id");

    // Create comment
    Integer commentId = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "content": "This is a test comment",
              "authorId": 1
            }
            """)
        .when().post("/tasks/" + taskId + "/comments")
        .then()
        .statusCode(201)
        .body("content", equalTo("This is a test comment"))
        .body("taskId", equalTo(taskId))
        .extract().path("id");

    // Get comments
    given()
        .when().get("/tasks/" + taskId + "/comments")
        .then()
        .statusCode(200)
        .body("size()", equalTo(1))
        .body("[0].content", equalTo("This is a test comment"));
  }

  @Test
  public void testDeleteComment() {
    // Create project and task with unique key
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

    Integer taskId = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Test Task"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then().statusCode(201)
        .extract().path("id");

    // Create comment
    Integer commentId = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "content": "Comment to delete",
              "authorId": 1
            }
            """)
        .when().post("/tasks/" + taskId + "/comments")
        .then().statusCode(201)
        .extract().path("id");

    // Delete comment
    given()
        .when().delete("/tasks/" + taskId + "/comments/" + commentId)
        .then()
        .statusCode(204);

    // Verify comment is deleted
    given()
        .when().get("/tasks/" + taskId + "/comments")
        .then()
        .statusCode(200)
        .body("size()", equalTo(0));
  }

  @Test
  public void testCreateCommentOnNonExistentTask() {
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "content": "Comment on non-existent task",
              "authorId": 1
            }
            """)
        .when().post("/tasks/999/comments")
        .then()
        .statusCode(404);
  }

  @Test
  public void testCreateCommentWithoutContent() {
    // Create project and task with unique key
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

    Integer taskId = given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "title": "Test Task"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then().statusCode(201)
        .extract().path("id");

    // Try to create comment without content
    given()
        .contentType(ContentType.JSON)
        .body("""
            {
              "authorId": 1
            }
            """)
        .when().post("/tasks/" + taskId + "/comments")
        .then()
        .statusCode(400);
  }
}