package org.taskmanagementapp.notification.service;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Notification Service API",
        version = "1.0"
    )
)
public class NotificationServiceApplication {

  public static void main(String[] args) {
    Micronaut.run(NotificationServiceApplication.class, args);
  }
}
