package org.taskmanagementapp.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taskmanagementapp.common.dto.UserDto;
import org.taskmanagementapp.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

  private final UserService userService;

  @GetMapping
  @Operation(summary = "Get all users")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get user by ID")
  public ResponseEntity<UserDto> getUserById(
      @Parameter(description = "User ID") @PathVariable("id") Long id) {
    return userService.getUserById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update user")
  public ResponseEntity<UserDto> updateUser(
      @Parameter(description = "User ID") @PathVariable("id") Long id,
      @RequestBody UserDto updates) {
    UserDto user = userService.updateUser(id, updates);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "User ID") @PathVariable("id") Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}