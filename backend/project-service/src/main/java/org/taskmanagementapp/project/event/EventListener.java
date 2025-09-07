package org.taskmanagementapp.project.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.taskmanagementapp.common.events.ActivityEventFactory;
import org.taskmanagementapp.project.service.EventPublisher;

@ApplicationScoped
public class EventListener {

  private static final Logger logger = LoggerFactory.getLogger(EventListener.class);

  @Inject
  EventPublisher eventPublisher;

  public void onProjectCreated(
      @Observes(during = TransactionPhase.AFTER_SUCCESS) ProjectCreatedEvent event) {
    try {
      eventPublisher.publishActivity(
          ActivityEventFactory.projectCreated(event.ownerId(), event.projectId(),
              event.projectName())
      );
      logger.debug("Published activity event for project creation: {}", event.projectName());
    } catch (Exception e) {
      logger.error("Failed to publish activity event for project: {}", event.projectName(), e);
    }
  }
}