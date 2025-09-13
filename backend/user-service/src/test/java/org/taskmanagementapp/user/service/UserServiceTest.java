package org.taskmanagementapp.user.service;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.taskmanagementapp.common.dto.UserDto;
import org.taskmanagementapp.common.enums.Role;
import org.taskmanagementapp.user.dto.AuthRequest;
import org.taskmanagementapp.user.dto.RegisterRequest;
import org.taskmanagementapp.user.entity.User;
import org.taskmanagementapp.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setId(1L);
    testUser.setName("John Doe");
    testUser.setEmail("john@example.com");
    testUser.setPassword("encoded-password");
    testUser.setRole(Role.USER);
  }

  @Test
  void testRegister() {
    RegisterRequest request = new RegisterRequest();
    request.setName("John Doe");
    request.setEmail("john@example.com");
    request.setPassword("password123");

    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
    when(userRepository.save(any(User.class))).thenReturn(testUser);
    when(jwtService.generateToken(anyString(), any(Long.class))).thenReturn("test-token");

    var result = userService.register(request);

    assertNotNull(result);
    assertEquals("test-token", result.getToken());
    assertEquals("John Doe", result.getUser().name());
    verify(userRepository).save(any(User.class));
  }

  @Test
  void testRegisterEmailExists() {
    RegisterRequest request = new RegisterRequest();
    request.setEmail("john@example.com");

    when(userRepository.existsByEmail(anyString())).thenReturn(true);

    assertThrows(RuntimeException.class, () -> userService.register(request));
  }

  @Test
  void testLogin() {
    AuthRequest request = new AuthRequest();
    request.setEmail("john@example.com");
    request.setPassword("password123");

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(jwtService.generateToken(anyString(), any(Long.class))).thenReturn("test-token");

    var result = userService.login(request);

    assertNotNull(result);
    assertEquals("test-token", result.getToken());
    assertEquals("john@example.com", result.getUser().email());
  }

  @Test
  void testLoginInvalidCredentials() {
    AuthRequest request = new AuthRequest();
    request.setEmail("john@example.com");
    request.setPassword("wrong-password");

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    assertThrows(RuntimeException.class, () -> userService.login(request));
  }

  @Test
  void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

    var result = userService.getAllUsers();

    assertEquals(1, result.size());
    assertEquals("John Doe", result.get(0).name());
  }

  @Test
  void testGetUserById() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

    var result = userService.getUserById(1L);

    assertTrue(result.isPresent());
    assertEquals("John Doe", result.get().name());
  }

  @Test
  void testGetCurrentUser() {
    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

    var result = userService.getCurrentUser("john@example.com");

    assertNotNull(result);
    assertEquals("john@example.com", result.email());
  }

  @Test
  void testUpdateUser() {
    var updates = new UserDto(1L, "john@example.com", "John Updated", "USER");

    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(userRepository.save(any(User.class))).thenReturn(testUser);

    var result = userService.updateUser(1L, updates);

    assertNotNull(result);
    verify(userRepository).save(any(User.class));
  }

  @Test
  void testDeleteUser() {
    userService.deleteUser(1L);
    verify(userRepository).deleteById(1L);
  }
}