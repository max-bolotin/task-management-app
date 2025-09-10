package org.taskmanagementapp.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.taskmanagementapp.common.dto.UserDto;
import org.taskmanagementapp.common.enums.Role;
import org.taskmanagementapp.user.dto.AuthRequest;
import org.taskmanagementapp.user.dto.AuthResponse;
import org.taskmanagementapp.user.dto.RegisterRequest;
import org.taskmanagementapp.user.entity.User;
import org.taskmanagementapp.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.USER);

    user = userRepository.save(user);

    String token = jwtService.generateToken(user.getEmail(), user.getId());
    return new AuthResponse(token, toDto(user));
  }

  public AuthResponse login(AuthRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    String token = jwtService.generateToken(user.getEmail(), user.getId());
    return new AuthResponse(token, toDto(user));
  }

  public UserDto getCurrentUser(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return toDto(user);
  }

  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(this::toDto)
        .toList();
  }

  public Optional<UserDto> getUserById(Long id) {
    return userRepository.findById(id)
        .map(this::toDto);
  }

  public UserDto updateUser(Long id, UserDto updates) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (updates.name() != null) {
      user.setName(updates.name());
    }
    if (updates.email() != null && !updates.email().equals(user.getEmail())) {
      if (userRepository.existsByEmail(updates.email())) {
        throw new RuntimeException("Email already exists");
      }
      user.setEmail(updates.email());
    }

    user = userRepository.save(user);
    return toDto(user);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  private UserDto toDto(User user) {
    return new UserDto(user.getId(), user.getEmail(), user.getName(), user.getRole().toString());
  }
}