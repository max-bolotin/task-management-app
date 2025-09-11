package org.taskmanagementapp.project;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseAuthenticatedTest {

  protected static final String VALID_JWT = createValidJWT();

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
      // Fallback token for tests
      return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwidXNlcklkIjoxLCJpYXQiOjE2OTkxMjM0NTYsImV4cCI6OTk5OTk5OTk5OX0.fallback";
    }
  }

  protected RequestSpecification givenAuth() {
    return given().header("Authorization", "Bearer " + VALID_JWT);
  }
}