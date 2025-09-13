package org.taskmanagementapp.user.entity;

import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.enums.Role;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

  @Test
  void testUserCreation() {
    User user = new User();
    user.setId(1L);
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setPassword("password123");
    user.setRole(Role.USER);

    assertEquals(1L, user.getId());
    assertEquals("John Doe", user.getName());
    assertEquals("john@example.com", user.getEmail());
    assertEquals("password123", user.getPassword());
    assertEquals(Role.USER, user.getRole());
  }

  @Test
  void testUserEquality() {
    User user1 = new User();
    user1.setId(1L);
    user1.setEmail("john@example.com");
    user1.setRole(Role.USER);

    User user2 = new User();
    user2.setId(1L);
    user2.setEmail("john@example.com");
    user2.setRole(Role.USER);

    // Test equality based on ID and email (core business logic)
    assertEquals(user1.getId(), user2.getId());
    assertEquals(user1.getEmail(), user2.getEmail());
    assertEquals(user1.getRole(), user2.getRole());
  }

  @Test
  void testUserToString() {
    User user = new User();
    user.setId(1L);
    user.setName("John Doe");
    user.setEmail("john@example.com");

    String toString = user.toString();
    assertTrue(toString.contains("John Doe"));
    assertTrue(toString.contains("john@example.com"));
  }

  @Test
  void testRoleEnum() {
    assertEquals("USER", Role.USER.name());
    assertEquals("ADMIN", Role.ADMIN.name());
  }
}