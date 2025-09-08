package org.taskmanagementapp.project.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProjectEntityTest {

  @Test
  @Transactional
  void testProjectCreation() {
    Project project = new Project();
    project.name = "Test Project";
    project.key = "TESTKEY";
    project.description = "Test Description";
    project.ownerId = 1L;

    project.persist();

    assertNotNull(project.id);
    assertNotNull(project.createdAt);
    assertNotNull(project.updatedAt);
    assertEquals("Test Project", project.name);
    assertEquals("TESTKEY", project.key);
  }

  @Test
  @Transactional
  void testProjectFindByKey() {
    Project project = new Project();
    project.name = "Find Test Project";
    project.key = "FINDKEY";
    project.ownerId = 1L;
    project.persist();

    var found = Project.find("key", "FINDKEY").firstResultOptional();
    assertTrue(found.isPresent());
    assertEquals("Find Test Project", ((Project) found.get()).name);
  }

  @Test
  @Transactional
  void testProjectUpdate() {
    Project project = new Project();
    project.name = "Original Name";
    project.key = "UPDATEKEY";
    project.ownerId = 1L;
    project.persist();

    project.name = "Updated Name";
    project.persist();

    assertEquals("Updated Name", project.name);
    assertNotNull(project.updatedAt);
  }
}