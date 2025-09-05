package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HealthStatus(
    String status,
    String service,
    LocalDateTime timestamp
) {

  public static HealthStatus up(String serviceName) {
    return new HealthStatus("UP", serviceName, LocalDateTime.now());
  }
}