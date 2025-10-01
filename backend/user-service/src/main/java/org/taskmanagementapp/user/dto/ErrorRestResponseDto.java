package org.taskmanagementapp.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
@Schema(description = "An error")
public class ErrorRestResponseDto implements RestDataTransport {

  @Schema(type = "string", description = "Error message", requiredMode = Schema.RequiredMode.REQUIRED, example = "Internal Server Error")
  String message;

  @Schema(type = "array", description = "Details of the error", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"418 I am a teapot.\"]")
  List<String> details;
}
