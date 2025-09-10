package org.taskmanagementapp.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskmanagementapp.common.enums.Role;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Role management endpoints")
public class RoleController {

  @GetMapping
  @Operation(summary = "Get all available roles")
  public ResponseEntity<List<Role>> getAllRoles() {
    return ResponseEntity.ok(Arrays.asList(Role.values()));
  }
}