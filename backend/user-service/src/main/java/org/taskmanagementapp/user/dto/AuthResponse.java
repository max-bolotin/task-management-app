package org.taskmanagementapp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.taskmanagementapp.common.dto.UserDto;

@Data
@AllArgsConstructor
public class AuthResponse {

  private String token;
  private UserDto user;
}