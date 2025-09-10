package org.taskmanagementapp.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.taskmanagementapp.common.enums.Role;
import org.taskmanagementapp.user.entity.User;
import org.taskmanagementapp.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminInitService implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${admin.email:admin@taskapp.com}")
  private String adminEmail;

  @Value("${admin.password:admin123}")
  private String adminPassword;

  @Override
  public void run(String... args) {
    if (!userRepository.existsByEmail(adminEmail)) {
      User admin = new User();
      admin.setName("Administrator");
      admin.setEmail(adminEmail);
      admin.setPassword(passwordEncoder.encode(adminPassword));
      admin.setRole(Role.ADMIN);

      userRepository.save(admin);
      log.info("Admin user created with email: {}", adminEmail);
    }
  }
}