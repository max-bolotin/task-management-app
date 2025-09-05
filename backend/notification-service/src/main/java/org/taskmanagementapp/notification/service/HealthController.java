package org.taskmanagementapp.notification.service;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.taskmanagementapp.common.dto.HealthStatus;

@Controller("/status")
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

  @Get
  @Operation(summary = "Health check", description = "Returns the health status of the notification service")
  public HttpResponse<HealthStatus> health() {
    return HttpResponse.ok(HealthStatus.up("notification-service"));
  }
}