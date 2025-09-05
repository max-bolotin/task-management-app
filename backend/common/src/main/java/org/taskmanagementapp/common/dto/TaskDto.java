package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.taskmanagementapp.common.enums.TaskStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskDto(
    Long id,

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    String title,

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    String description,

    @NotNull(message = "Status is required")
    TaskStatus status,

    @NotNull(message = "Project ID is required")
    Long projectId,

    Long assigneeId,
    Long reporterId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}