package org.taskmanagementapp.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskmanagementapp.common.dto.HealthStatus;

@RestController
@RequestMapping("/status")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

  @GetMapping
  @Operation(summary = "Health check", description = "Returns the health status of the user service")
  public ResponseEntity<HealthStatus> health() {
    return ResponseEntity.ok(HealthStatus.up("user-service"));
  }
}