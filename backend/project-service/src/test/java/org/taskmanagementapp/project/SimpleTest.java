package org.taskmanagementapp.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.enums.TaskStatus;

class SimpleTest {

  @Test
  void testTaskStatusEnum() {
    assertEquals("TODO", TaskStatus.TODO.name());
    assertEquals("IN_PROGRESS", TaskStatus.IN_PROGRESS.name());
    assertEquals("DONE", TaskStatus.DONE.name());
  }

  @Test
  void testBasicLogic() {
    String projectName = "Test Project";
    String projectKey = "TEST";

    assertNotNull(projectName);
    assertNotNull(projectKey);
    assertEquals(4, projectKey.length());
    assertTrue(projectName.contains("Test"));
  }
}