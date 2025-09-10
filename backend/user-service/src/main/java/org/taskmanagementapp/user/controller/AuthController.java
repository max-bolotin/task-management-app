package org.taskmanagementapp.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.taskmanagementapp.common.dto.UserDto;
import org.taskmanagementapp.user.dto.AuthRequest;
import org.taskmanagementapp.user.dto.AuthResponse;
import org.taskmanagementapp.user.dto.RegisterRequest;
import org.taskmanagementapp.user.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  @Operation(summary = "Register new user")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    AuthResponse response = userService.register(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  @Operation(summary = "Login user")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
    AuthResponse response = userService.login(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  @Operation(summary = "Get current user profile")
  public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
    UserDto user = userService.getCurrentUser(authentication.getName());
    return ResponseEntity.ok(user);
  }
}