package org.taskmanagementapp.notification.service.entity;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

  @Test
  void testDefaultConstructor() {
    Notification notification = new Notification();

    assertNull(notification.getId());
    assertNull(notification.getUserId());
    assertNull(notification.getMessage());
    assertNull(notification.getType());
    assertFalse(notification.isRead());
    assertNotNull(notification.getCreatedAt());
  }

  @Test
  void testParameterizedConstructor() {
    Long userId = 123L;
    String message = "Test message";
    String type = "TASK_ASSIGNED";

    Notification notification = new Notification(userId, message, type);

    assertEquals(userId, notification.getUserId());
    assertEquals(message, notification.getMessage());
    assertEquals(type, notification.getType());
    assertFalse(notification.isRead());
    assertNotNull(notification.getCreatedAt());
  }

  @Test
  void testSettersAndGetters() {
    Notification notification = new Notification();
    Instant now = Instant.now();

    notification.setId("test-id");
    notification.setUserId(456L);
    notification.setMessage("Updated message");
    notification.setType("TASK_COMMENTED");
    notification.setRead(true);
    notification.setCreatedAt(now);

    assertEquals("test-id", notification.getId());
    assertEquals(456L, notification.getUserId());
    assertEquals("Updated message", notification.getMessage());
    assertEquals("TASK_COMMENTED", notification.getType());
    assertTrue(notification.isRead());
    assertEquals(now, notification.getCreatedAt());
  }
}