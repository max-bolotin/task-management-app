package org.taskmanagementapp.notification.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.taskmanagementapp.common.dto.NotificationEvent;
import org.taskmanagementapp.notification.service.entity.Notification;
import org.taskmanagementapp.notification.service.repository.NotificationRepository;

@KafkaListener(groupId = "notification-service")
public class NotificationEventConsumer {

  private static final Logger logger = LoggerFactory.getLogger(NotificationEventConsumer.class);

  @Inject
  private NotificationRepository repository;

  @Inject
  private ObjectMapper objectMapper;

  @Topic("notification-events")
  public void processNotificationEvent(String eventJson) {
    try {
      logger.info("Received Kafka message: {}", eventJson);

      NotificationEvent event = objectMapper.readValue(eventJson, NotificationEvent.class);
      logger.info("Processing notification event: {} for user {}", event.eventType(),
          event.targetUserId());

      String message = createMessage(event);
      String type = event.eventType().toString();

      if (message == null || message.trim().isEmpty()) {
        logger.warn("Empty message for notification event: {}", event.eventType());
        return;
      }

      Notification notification = new Notification(event.targetUserId(), message, type);
      Notification saved = repository.save(notification);

      logger.info("Successfully saved notification with ID: {} for user {}: {}",
          saved.getId(), event.targetUserId(), message);

    } catch (Exception e) {
      logger.error("Failed to process notification event: {}", eventJson, e);
    }
  }

  private String createMessage(NotificationEvent event) {
    return switch (event.eventType()) {
      case TASK_ASSIGNED -> "You have been assigned a new task";
      case TASK_STATUS_CHANGED -> "Task's status has been changed";
      case TASK_COMMENTED -> "A task has been commented";
      case PROJECT_INVITATION -> "You were invited to a project";
      case MENTION -> "You were mentioned";
      default -> "New notification: " + event.eventType();
    };
  }
}