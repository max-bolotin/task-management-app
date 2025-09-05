package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentDto(
    Long id,

    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    String content,

    @NotNull(message = "Task ID is required")
    Long taskId,

    @NotNull(message = "Author ID is required")
    Long authorId,

    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}