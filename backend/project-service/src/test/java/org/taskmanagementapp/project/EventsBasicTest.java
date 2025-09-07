package org.taskmanagementapp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.enums.TaskStatus;

class EventsBasicTest {

  @Test
  void testTaskStatusEnum() {
    assertEquals("TODO", TaskStatus.TODO.name());
    assertEquals("IN_PROGRESS", TaskStatus.IN_PROGRESS.name());
    assertEquals("DONE", TaskStatus.DONE.name());
  }
}