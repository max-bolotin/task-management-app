package org.taskmanagementapp.notification.service.consumer;

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

  @Topic("notification-events")
  public void processNotificationEvent(NotificationEvent event) {
    try {
      logger.info("Processing notification event: {}", event.eventType());

      String message = createMessage(event);
      Notification notification = new Notification(event.targetUserId(), message,
          event.eventType().toString());

      repository.save(notification);
      logger.debug("Saved notification for user {}", event.targetUserId());

    } catch (Exception e) {
      logger.error("Failed to process notification event: {}", e.getMessage(), e);
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