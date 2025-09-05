package org.taskmanagementapp.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    String traceId
) {

  public static ErrorResponse of(int status, String error, String message, String path,
      String traceId) {
    return new ErrorResponse(LocalDateTime.now(), status, error, message, path, traceId);
  }
}