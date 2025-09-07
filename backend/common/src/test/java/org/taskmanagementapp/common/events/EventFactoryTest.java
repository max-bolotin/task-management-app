package org.taskmanagementapp.common.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.dto.ActivityEvent;
import org.taskmanagementapp.common.dto.NotificationEvent;
import org.taskmanagementapp.common.enums.ActivityEventType;
import org.taskmanagementapp.common.enums.NotificationEventType;

class EventFactoryTest {

  @Test
  void testActivityEventFactory() {
    ActivityEvent event = ActivityEventFactory.taskCreated(1L, 2L, 3L, "Test Task");

    assertEquals(ActivityEventType.TASK_CREATED, event.eventType());
    assertEquals(1L, event.userId());
    assertEquals(2L, event.projectId());
    assertEquals(3L, event.taskId());
    assertEquals("Created task: Test Task", event.description());
    assertNotNull(event.timestamp());
    assertTrue(event.metadata().containsKey("taskTitle"));
  }

  @Test
  void testNotificationEventFactory() {
    NotificationEvent event = NotificationEventFactory.taskAssigned(1L, 2L, 3L, 4L, "Test Task");

    assertEquals(NotificationEventType.TASK_ASSIGNED, event.eventType());
    assertEquals(2L, event.targetUserId());
    assertEquals(1L, event.actorUserId());
    assertEquals(3L, event.projectId());
    assertEquals(4L, event.taskId());
    assertEquals("You have been assigned to task: Test Task", event.message());
    assertNotNull(event.timestamp());
    assertTrue(event.metadata().containsKey("taskTitle"));
  }
}