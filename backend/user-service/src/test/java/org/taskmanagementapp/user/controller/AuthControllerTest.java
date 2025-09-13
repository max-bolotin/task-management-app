package org.taskmanagementapp.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.taskmanagementapp.common.dto.UserDto;
import org.taskmanagementapp.user.dto.AuthRequest;
import org.taskmanagementapp.user.dto.AuthResponse;
import org.taskmanagementapp.user.dto.RegisterRequest;
import org.taskmanagementapp.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})
@ContextConfiguration(classes = {AuthController.class})
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testRegister() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setName("John Doe");
    request.setEmail("john@example.com");
    request.setPassword("Password123!");

    UserDto userDto = new UserDto(1L, "john@example.com", "John Doe", "USER");
    AuthResponse response = new AuthResponse("test-token", userDto);

    when(userService.register(any(RegisterRequest.class))).thenReturn(response);

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value("test-token"))
        .andExpect(jsonPath("$.user.name").value("John Doe"));
  }

  @Test
  void testLogin() throws Exception {
    AuthRequest request = new AuthRequest();
    request.setEmail("john@example.com");
    request.setPassword("Password123!");

    UserDto userDto = new UserDto(1L, "john@example.com", "John Doe", "USER");
    AuthResponse response = new AuthResponse("test-token", userDto);

    when(userService.login(any(AuthRequest.class))).thenReturn(response);

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("test-token"))
        .andExpect(jsonPath("$.user.email").value("john@example.com"));
  }

  @Test
  void testGetCurrentUser() throws Exception {
    UserDto userDto = new UserDto(1L, "john@example.com", "John Doe", "USER");

    when(userService.getCurrentUser("test-user")).thenReturn(userDto);

    mockMvc.perform(get("/auth/me")
            .header("Authorization", "Bearer test-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.email").value("john@example.com"));
  }
}