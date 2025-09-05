package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationRequest(
    @NotBlank(message = "Channel ID is required")
    String channelId,

    @NotBlank(message = "Message is required")
    String message,

    @NotNull(message = "User ID is required")
    Long userId,

    Long projectId,
    Long taskId,
    Map<String, Object> metadata
) {

}