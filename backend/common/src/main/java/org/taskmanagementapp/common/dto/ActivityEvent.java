package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import org.taskmanagementapp.common.enums.ActivityEventType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ActivityEvent(
    @NotNull(message = "Event type is required")
    ActivityEventType eventType,

    @NotNull(message = "User ID is required")
    Long userId,

    Long projectId,
    Long taskId,
    Long commentId,
    String description,
    Map<String, Object> metadata,
    LocalDateTime timestamp
) {

  public ActivityEvent {
    if (timestamp == null) {
      timestamp = LocalDateTime.now();
    }
  }
}