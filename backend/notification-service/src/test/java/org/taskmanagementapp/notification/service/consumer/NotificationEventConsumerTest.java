package org.taskmanagementapp.notification.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.taskmanagementapp.common.dto.NotificationEvent;
import org.taskmanagementapp.common.enums.NotificationEventType;
import org.taskmanagementapp.notification.service.entity.Notification;
import org.taskmanagementapp.notification.service.repository.NotificationRepository;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MicronautTest
class NotificationEventConsumerTest {

  @Inject
  NotificationEventConsumer consumer;

  @Inject
  NotificationRepository repository;

  @Inject
  ObjectMapper objectMapper;

  @MockBean(NotificationRepository.class)
  NotificationRepository mockRepository() {
    return mock(NotificationRepository.class);
  }

  @MockBean(ObjectMapper.class)
  ObjectMapper mockObjectMapper() {
    return mock(ObjectMapper.class);
  }

  @Test
  void shouldCreateNotificationForTaskAssignedEvent() throws Exception {
    String eventJson = "{\"eventType\":\"TASK_ASSIGNED\",\"targetUserId\":123}";
    NotificationEvent event = new NotificationEvent(
        NotificationEventType.TASK_ASSIGNED, 123L, 456L, 1L, 2L, null, null, null,
        LocalDateTime.now());

    when(objectMapper.readValue(eventJson, NotificationEvent.class)).thenReturn(event);
    when(repository.save(any(Notification.class))).thenReturn(new Notification());

    consumer.processNotificationEvent(eventJson);

    verify(repository).save(argThat(n ->
        n.getUserId().equals(123L) &&
            n.getMessage().equals("You have been assigned a new task")));
  }

  @Test
  void shouldCreateCorrectMessageForDifferentEventTypes() throws Exception {
    testEventTypeMessage(NotificationEventType.TASK_STATUS_CHANGED,
        "Task's status has been changed");
    testEventTypeMessage(NotificationEventType.TASK_COMMENTED, "A task has been commented");
    testEventTypeMessage(NotificationEventType.PROJECT_INVITATION, "You were invited to a project");
    testEventTypeMessage(NotificationEventType.MENTION, "You were mentioned");
  }

  private void testEventTypeMessage(NotificationEventType eventType, String expectedMessage)
      throws Exception {
    NotificationEvent event = new NotificationEvent(eventType, 123L, 456L, 1L, 2L, null, null, null,
        LocalDateTime.now());
    String eventJson = "{\"eventType\":\"" + eventType + "\",\"targetUserId\":123}";

    when(objectMapper.readValue(eventJson, NotificationEvent.class)).thenReturn(event);
    when(repository.save(any(Notification.class))).thenReturn(new Notification());

    consumer.processNotificationEvent(eventJson);

    verify(repository).save(argThat(n -> n.getMessage().equals(expectedMessage)));
    reset(repository, objectMapper);
  }

  @Test
  void shouldHandleInvalidJsonGracefully() throws Exception {
    when(objectMapper.readValue(anyString(), eq(NotificationEvent.class)))
        .thenThrow(new RuntimeException("Invalid JSON"));

    consumer.processNotificationEvent("invalid json");

    verify(repository, never()).save(any());
  }

  @Test
  void shouldHandleDatabaseErrorsGracefully() throws Exception {
    NotificationEvent event = new NotificationEvent(
        NotificationEventType.TASK_ASSIGNED, 123L, 456L, null, null, null, null, null,
        LocalDateTime.now());

    when(objectMapper.readValue(anyString(), eq(NotificationEvent.class))).thenReturn(event);
    when(repository.save(any(Notification.class))).thenThrow(
        new RuntimeException("Database error"));

    consumer.processNotificationEvent("{\"eventType\":\"TASK_ASSIGNED\",\"targetUserId\":123}");

    verify(repository).save(any(Notification.class));
  }
}