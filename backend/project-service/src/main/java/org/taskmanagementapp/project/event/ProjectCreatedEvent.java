package org.taskmanagementapp.project.event;

public record ProjectCreatedEvent(Long projectId, String projectName, Long ownerId) {

}