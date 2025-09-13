package org.taskmanagementapp.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.taskmanagementapp.common.dto.UserDto;
import org.taskmanagementapp.user.service.UserService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class})
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void testGetAllUsers() throws Exception {
    UserDto user1 = new UserDto(1L, "john@example.com", "John Doe", "USER");
    UserDto user2 = new UserDto(2L, "jane@example.com", "Jane Smith", "USER");

    when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("John Doe"))
        .andExpect(jsonPath("$[1].name").value("Jane Smith"));
  }

  @Test
  @WithMockUser
  void testGetUserById() throws Exception {
    UserDto user = new UserDto(1L, "john@example.com", "John Doe", "USER");

    when(userService.getUserById(1L)).thenReturn(Optional.of(user));

    mockMvc.perform(get("/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.email").value("john@example.com"));
  }

  @Test
  @WithMockUser
  void testUpdateUser() throws Exception {
    UserDto updates = new UserDto(1L, "john.updated@example.com", "John Updated", "USER");
    UserDto updatedUser = new UserDto(1L, "john.updated@example.com", "John Updated", "USER");

    when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updatedUser);

    mockMvc.perform(patch("/users/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updates)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("John Updated"))
        .andExpect(jsonPath("$.email").value("john.updated@example.com"));
  }

  @Test
  @WithMockUser
  void testDeleteUser() throws Exception {
    doNothing().when(userService).deleteUser(1L);

    mockMvc.perform(delete("/users/1")
            .with(csrf()))
        .andExpect(status().isNoContent());
  }
}