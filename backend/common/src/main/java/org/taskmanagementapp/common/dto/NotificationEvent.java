package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import org.taskmanagementapp.common.enums.NotificationEventType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationEvent(
    @NotNull(message = "Event type is required")
    NotificationEventType eventType,

    @NotNull(message = "Target user ID is required")
    Long targetUserId,

    @NotNull(message = "Actor user ID is required")
    Long actorUserId,

    Long projectId,
    Long taskId,
    Long commentId,
    String message,
    Map<String, Object> metadata,
    LocalDateTime timestamp
) {

  public NotificationEvent {
    if (timestamp == null) {
      timestamp = LocalDateTime.now();
    }
  }
}