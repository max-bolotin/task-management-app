package org.taskmanagementapp.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.taskmanagementapp.user.service.JwtService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class JwtAuthenticationTest {

  @Autowired
  private JwtService jwtService;

  @Test
  public void testJwtTokenGeneration() {
    String email = "test@example.com";
    Long userId = 1L;

    String token = jwtService.generateToken(email, userId);

    assertNotNull(token);
    assertFalse(token.isEmpty());
    assertTrue(token.startsWith("eyJ")); // JWT tokens start with eyJ
  }

  @Test
  public void testJwtTokenValidation() {
    String email = "test@example.com";
    Long userId = 1L;

    String token = jwtService.generateToken(email, userId);

    assertTrue(jwtService.isTokenValid(token));
    assertEquals(email, jwtService.extractEmail(token));
    assertEquals(userId, jwtService.extractUserId(token));
  }

  @Test
  public void testInvalidJwtToken() {
    String invalidToken = "invalid.jwt.token";

    assertFalse(jwtService.isTokenValid(invalidToken));
  }

  @Test
  public void testJwtClaimsExtraction() {
    String email = "claims@example.com";
    Long userId = 42L;

    String token = jwtService.generateToken(email, userId);

    assertEquals(email, jwtService.extractEmail(token));
    assertEquals(userId, jwtService.extractUserId(token));
  }

  @Test
  public void testEmptyToken() {
    assertFalse(jwtService.isTokenValid(""));
    assertFalse(jwtService.isTokenValid(null));
  }
}