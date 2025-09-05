package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ActivityEvent(
    Long id,

    @NotBlank(message = "Event type is required")
    String eventType,

    @NotNull(message = "User ID is required")
    Long userId,

    Long projectId,
    Long taskId,
    String description,
    LocalDateTime timestamp
) {

}