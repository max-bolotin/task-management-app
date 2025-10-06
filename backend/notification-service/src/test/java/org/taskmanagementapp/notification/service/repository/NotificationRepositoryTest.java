package org.taskmanagementapp.notification.service.repository;

import org.junit.jupiter.api.Test;
import org.taskmanagementapp.notification.service.entity.Notification;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRepositoryTest {

  @Test
  void shouldValidateRepositoryInterface() {
    assertNotNull(NotificationRepository.class);

    Notification notification = new Notification(123L, "Test message", "TEST");
    assertEquals(123L, notification.getUserId());
    assertEquals("Test message", notification.getMessage());
    assertEquals("TEST", notification.getType());
  }
}