package org.taskmanagementapp.project.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.taskmanagementapp.common.dto.ActivityEvent;
import org.taskmanagementapp.common.dto.NotificationEvent;

@ApplicationScoped
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    @Inject
    @Channel("activity-events")
    Emitter<ActivityEvent> activityEmitter;

    @Inject
    @Channel("notification-events")
    Emitter<NotificationEvent> notificationEmitter;

    public void publishActivity(ActivityEvent event) {
        try {
            activityEmitter.send(event);
            logger.debug("Published activity event: {}", event.eventType());
        } catch (Exception e) {
            logger.error("Failed to publish activity event: {}", event.eventType(), e);
        }
    }

    public void publishNotification(NotificationEvent event) {
        try {
            notificationEmitter.send(event);
            logger.debug("Published notification event: {}", event.eventType());
        } catch (Exception e) {
            logger.error("Failed to publish notification event: {}", event.eventType(), e);
        }
    }
}