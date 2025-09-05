package org.taskmanagementapp.common.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.enums.Role;
import org.taskmanagementapp.common.enums.TaskStatus;
import org.taskmanagementapp.common.error.ErrorResponse;

class DtoTest {

  @Test
  void testUserDto() {
    var user = new UserDto(1L, "test@example.com", "Test User", Role.USER.name());
    assertEquals(1L, user.id());
    assertEquals("test@example.com", user.email());
    assertEquals("Test User", user.name());
    assertEquals("USER", user.role());
  }

  @Test
  void testProjectDto() {
    var project = new ProjectDto(1L, "Test Project", "TEST", "Description", 1L, null, null);
    assertEquals("TEST", project.key());
    assertEquals("Test Project", project.name());
  }

  @Test
  void testTaskDto() {
    var task = new TaskDto(1L, "Test Task", "Description", TaskStatus.TODO, 1L, 2L, 3L, null, null);
    assertEquals(TaskStatus.TODO, task.status());
    assertEquals("Test Task", task.title());
  }

  @Test
  void testErrorResponse() {
    var error = ErrorResponse.of(404, "Not Found", "Resource not found", "/api/test", "trace-123");
    assertEquals(404, error.status());
    assertEquals("Not Found", error.error());
    assertNotNull(error.timestamp());
  }
}