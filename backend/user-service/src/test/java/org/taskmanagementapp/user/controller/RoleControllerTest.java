package org.taskmanagementapp.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
@ContextConfiguration(classes = {RoleController.class})
class RoleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser
  void testGetRoles() throws Exception {
    mockMvc.perform(get("/roles"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0]").value("ADMIN"))
        .andExpect(jsonPath("$[1]").value("USER"));
  }
}