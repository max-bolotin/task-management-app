package org.taskmanagementapp.notification.service.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.taskmanagementapp.notification.service.entity.Notification;
import org.taskmanagementapp.notification.service.repository.NotificationRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller("/notifications")
@Tag(name = "Notifications", description = "Notification management")
public class NotificationController {

  @Inject
  private NotificationRepository repository;

  @Get("/{userId}")
  @Operation(summary = "Get user notifications", description = "Retrieve all notifications for a user")
  public List<Notification> getUserNotifications(@PathVariable Long userId) {
    return repository.findByUserIdOrderByCreatedAtDesc(userId);
  }

  @Post("/{userId}")
  @Operation(summary = "Create notification", description = "Create a new notification for a user")
  public HttpResponse<Notification> createNotification(@PathVariable Long userId,
      @Body Map<String, String> request) {
    String message = request.get("message");
    String type = request.getOrDefault("type", "MANUAL");

    if (message == null || message.trim().isEmpty()) {
      return HttpResponse.badRequest();
    }

    Notification notification = new Notification(userId, message, type);
    Notification saved = repository.save(notification);
    return HttpResponse.ok(saved);
  }

  @Patch("/{id}/read")
  @Operation(summary = "Mark notification as read", description = "Mark a notification as read")
  public HttpResponse<Notification> markAsRead(@PathVariable String id) {
    Optional<Notification> notification = repository.findById(id);
    if (notification.isEmpty()) {
      return HttpResponse.notFound();
    }

    Notification n = notification.get();
    n.setRead(true);
    repository.update(n);
    return HttpResponse.ok(n);
  }

  @Delete("/{id}")
  @Operation(summary = "Delete notification", description = "Delete a notification")
  public HttpResponse<Void> deleteNotification(@PathVariable String id) {
    if (!repository.existsById(id)) {
      return HttpResponse.notFound();
    }
    repository.deleteById(id);
    return HttpResponse.noContent();
  }
}