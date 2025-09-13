package org.taskmanagementapp.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService();
    ReflectionTestUtils.setField(jwtService, "secret",
        "myVerySecretKeyForJWTTokenGeneration123456789");
    ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
  }

  @Test
  void testGenerateToken() {
    String token = jwtService.generateToken("john@example.com", 1L);

    assertNotNull(token);
    assertFalse(token.isEmpty());
    assertTrue(token.contains("."));
  }

  @Test
  void testExtractEmail() {
    String token = jwtService.generateToken("john@example.com", 1L);
    String email = jwtService.extractEmail(token);

    assertEquals("john@example.com", email);
  }

  @Test
  void testExtractUserId() {
    String token = jwtService.generateToken("john@example.com", 1L);
    Long userId = jwtService.extractUserId(token);

    assertEquals(1L, userId);
  }

  @Test
  void testIsTokenValid() {
    String token = jwtService.generateToken("john@example.com", 1L);
    boolean isValid = jwtService.isTokenValid(token);

    assertTrue(isValid);
  }

  @Test
  void testIsTokenInvalid() {
    boolean isValid = jwtService.isTokenValid("invalid.token.here");

    assertFalse(isValid);
  }
}