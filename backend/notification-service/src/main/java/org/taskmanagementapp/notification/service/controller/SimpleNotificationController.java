package org.taskmanagementapp.notification.service.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("/notifications")
@Tag(name = "Notifications", description = "Notification management endpoints")
public class SimpleNotificationController {

  private final List<Map<String, Object>> notifications = new ArrayList<>();

  @Get("/{userId}")
  @Operation(summary = "Get user notifications", description = "Retrieve all notifications for a specific user")
  public List<Map<String, Object>> getUserNotifications(@PathVariable Long userId) {
    return notifications.stream()
        .filter(n -> userId.equals(n.get("userId")))
        .toList();
  }

  @Post("/test/{userId}")
  @Operation(summary = "Create test notification", description = "Create a test notification for testing")
  public HttpResponse<Map<String, Object>> createTestNotification(@PathVariable Long userId,
      @Body Map<String, String> request) {
    Map<String, Object> notification = Map.of(
        "id", System.currentTimeMillis(),
        "userId", userId,
        "message", request.getOrDefault("message", "Test notification"),
        "type", request.getOrDefault("type", "TEST"),
        "read", false,
        "createdAt", Instant.now()
    );
    notifications.add(notification);
    return HttpResponse.ok(notification);
  }
}