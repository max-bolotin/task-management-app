package org.taskmanagementapp.project.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.enums.TaskStatus;

@QuarkusTest
class TaskEntityTest {

  @Test
  @Transactional
  void testTaskCreation() {
    // Create project first
    var project = new Project();
    project.name = "Task Test Project";
    project.key = "TASKPROJ";
    project.ownerId = 1L;
    project.persist();

    var task = new Task();
    task.title = "Test Task";
    task.description = "Test Description";
    task.projectId = project.id;
    task.assigneeId = 2L;

    task.persist();

    assertNotNull(task.id);
    assertNotNull(task.createdAt);
    assertNotNull(task.updatedAt);
    assertEquals("Test Task", task.title);
    assertEquals(TaskStatus.TODO, task.status); // Default status
    assertEquals(project.id, task.projectId);
  }

  @Test
  @Transactional
  void testTaskFindByProject() {
    // Create project
    var project = new Project();
    project.name = "Find Task Project";
    project.key = "FINDTASK";
    project.ownerId = 1L;
    project.persist();

    // Create tasks
    var task1 = new Task();
    task1.title = "Task 1";
    task1.projectId = project.id;
    task1.persist();

    var task2 = new Task();
    task2.title = "Task 2";
    task2.projectId = project.id;
    task2.persist();

    var tasks = Task.find("projectId", project.id).list();
    assertEquals(2, tasks.size());
  }

  @Test
  @Transactional
  void testTaskStatusUpdate() {
    // Create project first
    var project = new Project();
    project.name = "Status Test Project";
    project.key = "STATUSPROJ";
    project.ownerId = 1L;
    project.persist();

    var task = new Task();
    task.title = "Status Test Task";
    task.projectId = project.id;
    task.persist();

    assertEquals(TaskStatus.TODO, task.status);

    task.status = TaskStatus.IN_PROGRESS;
    task.persist();

    assertEquals(TaskStatus.IN_PROGRESS, task.status);
  }
}