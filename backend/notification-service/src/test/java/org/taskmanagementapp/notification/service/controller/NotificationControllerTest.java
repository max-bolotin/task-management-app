package org.taskmanagementapp.notification.service.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.taskmanagementapp.notification.service.entity.Notification;
import org.taskmanagementapp.notification.service.repository.NotificationRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MicronautTest
class NotificationControllerTest {

  @Inject
  NotificationController controller;

  @Inject
  NotificationRepository repository;

  @MockBean(NotificationRepository.class)
  NotificationRepository mockRepository() {
    return mock(NotificationRepository.class);
  }

  @Test
  void shouldReturnUserNotifications() {
    Long userId = 123L;
    List<Notification> notifications = List.of(
        new Notification(userId, "Message 1", "TASK_ASSIGNED"),
        new Notification(userId, "Message 2", "TASK_COMMENTED")
    );

    when(repository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(notifications);

    List<Notification> result = controller.getUserNotifications(userId);

    assertEquals(2, result.size());
    assertEquals("Message 1", result.get(0).getMessage());
  }

  @Test
  void shouldCreateNotificationSuccessfully() {
    Long userId = 123L;
    Map<String, String> request = Map.of("message", "Test notification", "type", "MANUAL");
    Notification savedNotification = new Notification(userId, "Test notification", "MANUAL");

    when(repository.save(any(Notification.class))).thenReturn(savedNotification);

    HttpResponse<Notification> response = controller.createNotification(userId, request);

    assertEquals(HttpStatus.OK, response.getStatus());
    assertEquals("Test notification", response.body().getMessage());
  }

  @Test
  void shouldRejectEmptyMessage() {
    Long userId = 123L;
    Map<String, String> request = Map.of("message", "");

    HttpResponse<Notification> response = controller.createNotification(userId, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    verify(repository, never()).save(any());
  }

  @Test
  void shouldRejectNullMessage() {
    Long userId = 123L;
    Map<String, String> request = Map.of("type", "MANUAL");

    HttpResponse<Notification> response = controller.createNotification(userId, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    verify(repository, never()).save(any());
  }

  @Test
  void shouldUseDefaultTypeWhenNotProvided() {
    Long userId = 123L;
    Map<String, String> request = Map.of("message", "Test message");

    when(repository.save(any(Notification.class))).thenReturn(new Notification());

    HttpResponse<Notification> response = controller.createNotification(userId, request);

    assertEquals(HttpStatus.OK, response.getStatus());
    verify(repository).save(argThat(n -> "MANUAL".equals(n.getType())));
  }

  @Test
  void shouldMarkNotificationAsRead() {
    String notificationId = "test-id";
    Notification notification = new Notification(123L, "Test", "TASK_ASSIGNED");
    notification.setId(notificationId);

    when(repository.findById(notificationId)).thenReturn(Optional.of(notification));
    when(repository.update(notification)).thenReturn(notification);

    HttpResponse<Notification> response = controller.markAsRead(notificationId);

    assertEquals(HttpStatus.OK, response.getStatus());
    assertTrue(response.body().isRead());
  }

  @Test
  void shouldReturnNotFoundForNonExistentNotification() {
    when(repository.findById("non-existent")).thenReturn(Optional.empty());

    HttpResponse<Notification> response = controller.markAsRead("non-existent");

    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    verify(repository, never()).update(any());
  }

  @Test
  void shouldDeleteNotificationSuccessfully() {
    String notificationId = "test-id";
    when(repository.existsById(notificationId)).thenReturn(true);

    HttpResponse<Void> response = controller.deleteNotification(notificationId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    verify(repository).deleteById(notificationId);
  }

  @Test
  void shouldReturnNotFoundWhenDeletingNonExistentNotification() {
    String notificationId = "non-existent";
    when(repository.existsById(notificationId)).thenReturn(false);

    HttpResponse<Void> response = controller.deleteNotification(notificationId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    verify(repository, never()).deleteById(any());
  }
}