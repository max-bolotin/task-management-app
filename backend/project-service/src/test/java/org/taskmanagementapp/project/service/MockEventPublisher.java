package org.taskmanagementapp.project.service;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import org.taskmanagementapp.common.dto.ActivityEvent;
import org.taskmanagementapp.common.dto.NotificationEvent;

@Mock
@ApplicationScoped
public class MockEventPublisher extends EventPublisher {

    @Override
    public void publishActivity(ActivityEvent event) {
        // Mock implementation - do nothing
    }

    @Override
    public void publishNotification(NotificationEvent event) {
        // Mock implementation - do nothing
    }
}