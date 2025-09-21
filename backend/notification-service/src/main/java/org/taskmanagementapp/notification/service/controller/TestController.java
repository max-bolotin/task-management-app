package org.taskmanagementapp.notification.service.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/test")
@Tag(name = "Test", description = "Test endpoints")
public class TestController {

  @Get
  @Operation(summary = "Test endpoint", description = "Simple test endpoint without dependencies")
  public String test() {
    return "Notification Service is running!";
  }
}