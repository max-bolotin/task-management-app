package org.taskmanagementapp.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.taskmanagementapp.user.service.JwtService;

import static org.junit.jupiter.api.Assertions.*;

public class JwtAuthenticationTest {

  private JwtService jwtService;

  private JwtService createJwtService() {
    JwtService service = new JwtService();
    try {
      var secretField = JwtService.class.getDeclaredField("secret");
      secretField.setAccessible(true);
      secretField.set(service, "test-secret-key-for-jwt-testing-purposes");

      var expirationField = JwtService.class.getDeclaredField("expiration");
      expirationField.setAccessible(true);
      expirationField.set(service, 86400000L); // 24 hours

      return service;
    } catch (Exception e) {
      fail("Failed to set up JWT service: " + e.getMessage());
      return null;
    }
  }

  @Test
  public void testJwtTokenGeneration() {
    JwtService jwtService = createJwtService();
    String email = "test@example.com";
    Long userId = 1L;

    String token = jwtService.generateToken(email, userId);

    assertNotNull(token);
    assertFalse(token.isEmpty());
    assertTrue(token.startsWith("eyJ")); // JWT tokens start with eyJ
  }

  @Test
  public void testJwtTokenValidation() {
    JwtService jwtService = createJwtService();
    String email = "test@example.com";
    Long userId = 1L;

    String token = jwtService.generateToken(email, userId);

    assertTrue(jwtService.isTokenValid(token));
    assertEquals(email, jwtService.extractEmail(token));
    assertEquals(userId, jwtService.extractUserId(token));
  }

  @Test
  public void testInvalidJwtToken() {
    JwtService jwtService = createJwtService();
    String invalidToken = "invalid.jwt.token";

    assertFalse(jwtService.isTokenValid(invalidToken));
  }

  @Test
  public void testJwtTokenExpiration() {
    // Create a token with very short expiration for testing
    try {
      JwtService jwtService = new JwtService();
      var secretField = JwtService.class.getDeclaredField("secret");
      secretField.setAccessible(true);
      secretField.set(jwtService, "test-secret-key-for-jwt-testing-purposes");

      var expirationField = JwtService.class.getDeclaredField("expiration");
      expirationField.setAccessible(true);
      expirationField.set(jwtService, 1L); // 1 millisecond

      String token = jwtService.generateToken("test@example.com", 1L);

      // Wait for token to expire
      Thread.sleep(10);

      // Token should be invalid due to expiration
      assertFalse(jwtService.isTokenValid(token));
    } catch (Exception e) {
      fail("Test failed: " + e.getMessage());
    }
  }

  @Test
  public void testJwtClaimsExtraction() {
    JwtService jwtService = createJwtService();
    String email = "claims@example.com";
    Long userId = 42L;

    String token = jwtService.generateToken(email, userId);

    assertEquals(email, jwtService.extractEmail(token));
    assertEquals(userId, jwtService.extractUserId(token));
  }

  @Test
  public void testEmptyToken() {
    JwtService jwtService = createJwtService();
    assertFalse(jwtService.isTokenValid(""));
    assertFalse(jwtService.isTokenValid(null));
  }
}