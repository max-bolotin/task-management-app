package org.taskmanagementapp.common.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.dto.ActivityEvent;
import org.taskmanagementapp.common.dto.NotificationEvent;

class EventFactoryTest {

  @Test
  void testTaskStatusChangeEventContainsOldAndNewStatus() {
    ActivityEvent event = ActivityEventFactory.taskStatusChanged(
        1L, 2L, 3L, "TODO", "IN_PROGRESS", "Fix bug"
    );

    assertEquals("TODO", event.metadata().get("oldStatus"));
    assertEquals("IN_PROGRESS", event.metadata().get("newStatus"));
    assertTrue(event.description().contains("TODO"));
    assertTrue(event.description().contains("IN_PROGRESS"));
  }

  @Test
  void testTaskAssignmentNotificationTargetsCorrectUser() {
    Long actorId = 100L;
    Long assigneeId = 200L;

    NotificationEvent event = NotificationEventFactory.taskAssigned(
        actorId, assigneeId, 1L, 2L, "Important Task"
    );

    assertEquals(assigneeId, event.targetUserId());
    assertEquals(actorId, event.actorUserId());
    assertNotEquals(event.targetUserId(), event.actorUserId());
  }
}