package org.taskmanagementapp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

  @NotBlank
  private String name;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character")
  private String password;
}