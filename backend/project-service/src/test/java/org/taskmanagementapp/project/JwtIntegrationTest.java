package org.taskmanagementapp.project;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class JwtIntegrationTest {

  // Create a valid JWT token for testing with the same secret as the filter
  private static final String VALID_JWT = createValidJWT();
  private static final String INVALID_JWT = "invalid.jwt.token";

  private static String createValidJWT() {
    try {
      String secret = System.getProperty("jwt.secret",
          "myVerySecretKeyForJWTTokenGeneration123456789");
      javax.crypto.SecretKey key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes());

      return io.jsonwebtoken.Jwts.builder()
          .subject("test@example.com")
          .claim("userId", 1L)
          .issuedAt(new java.util.Date())
          .expiration(new java.util.Date(System.currentTimeMillis() + 86400000)) // 24 hours
          .signWith(key)
          .compact();
    } catch (Exception e) {
      return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwidXNlcklkIjoxLCJpYXQiOjE2OTkxMjM0NTYsImV4cCI6OTk5OTk5OTk5OX0.fallback-token";
    }
  }

  @Test
  public void testProjectAccessWithValidJWT() {
    given()
        .header("Authorization", "Bearer " + VALID_JWT)
        .when().get("/projects")
        .then()
        .statusCode(200);
  }

  @Test
  public void testStatusWithValidJWT() {
    given()
        .header("Authorization", "Bearer " + VALID_JWT)
        .when().get("/status")
        .then()
        .statusCode(200);
  }

  @Test
  public void testProjectAccessWithoutJWT() {
    given()
        .when().get("/projects")
        .then()
        .statusCode(401);
  }

  @Test
  public void testProjectAccessWithInvalidJWT() {
    given()
        .header("Authorization", "Bearer " + INVALID_JWT)
        .when().get("/projects")
        .then()
        .statusCode(401);
  }

  @Test
  public void testTaskCreationWithJWT() {
    // First create a project with unique key
    String uniqueKey = "JWT" + System.currentTimeMillis();
    Integer projectId = given()
        .header("Authorization", "Bearer " + VALID_JWT)
        .contentType(ContentType.JSON)
        .body(String.format("""
            {
                "name": "Test Project",
                "key": "%s",
                "description": "Test project for JWT",
                "ownerId": 1
            }
            """, uniqueKey))
        .when().post("/projects")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .extract().path("id");

    // Create task with JWT - should auto-assign to user
    given()
        .header("Authorization", "Bearer " + VALID_JWT)
        .contentType(ContentType.JSON)
        .body("""
            {
                "title": "Test Task",
                "description": "Test task creation",
                "status": "TODO"
            }
            """)
        .when().post("/projects/" + projectId + "/tasks")
        .then()
        .statusCode(201)
        .body("assigneeId", is(1))
        .body("reporterId", is(1));
  }

  @Test
  public void testHealthEndpointNoAuth() {
    // Status endpoint requires authentication in this service
    // Test that it returns 401 without auth (expected behavior)
    given()
        .when().get("/status")
        .then()
        .statusCode(401);
  }
}