package org.taskmanagementapp.common.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class HealthStatusTest {

  @Test
  void testHealthStatusUp() {
    var health = HealthStatus.up("test-service");

    assertEquals("UP", health.status());
    assertEquals("test-service", health.service());
    assertNotNull(health.timestamp());
  }

  @Test
  void testHealthStatusRecord() {
    var health = new HealthStatus("DOWN", "test-service", null);

    assertEquals("DOWN", health.status());
    assertEquals("test-service", health.service());
    assertNull(health.timestamp());
  }
}