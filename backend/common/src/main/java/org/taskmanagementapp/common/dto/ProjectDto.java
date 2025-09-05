package org.taskmanagementapp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjectDto(
    Long id,

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @NotBlank(message = "Key is required")
    @Pattern(regexp = "^[A-Z][A-Z0-9]{1,9}$", message = "Key must start with letter, be 2-10 chars, uppercase alphanumeric")
    String key,

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description,

    Long ownerId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}