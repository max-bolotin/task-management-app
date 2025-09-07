package org.taskmanagementapp.project.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.taskmanagementapp.common.enums.TaskStatus;
import org.taskmanagementapp.common.events.ActivityEventFactory;
import org.taskmanagementapp.common.events.NotificationEventFactory;
import org.taskmanagementapp.project.entity.Project;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.exception.ValidationException;

@ApplicationScoped
public class TaskService {

  @Inject
  EventPublisher eventPublisher;

  public List<Task> getTasksByProject(Long projectId) {
    if (Project.findById(projectId) == null) {
      throw new NotFoundException("Project not found");
    }
    return Task.find("projectId", projectId).list();
  }

  @Transactional
  public Task createTask(Long projectId, Task task) {
    if (Project.findById(projectId) == null) {
      throw new NotFoundException("Project not found");
    }

    if (task.title == null || task.title.trim().isEmpty()) {
      throw new ValidationException("Task title is required");
    }

    task.id = null;
    task.projectId = projectId;
    task.persist();

    // Publish activity event
    eventPublisher.publishActivity(
        ActivityEventFactory.taskCreated(task.reporterId != null ? task.reporterId : 1L, projectId, task.id, task.title)
    );

    return task;
  }

  public Task getTaskById(Long taskId) {
    Task task = Task.findById(taskId);
    if (task == null) {
      throw new NotFoundException("Task not found");
    }
    return task;
  }

  @Transactional
  public Task updateTask(Long taskId, Task updates) {
    Task task = Task.findById(taskId);
    if (task == null) {
      throw new NotFoundException("Task not found");
    }

    if (updates.title != null) {
      task.title = updates.title;
    }
    if (updates.description != null) {
      task.description = updates.description;
    }
    if (updates.status != null) {
      task.status = updates.status;
    }
    if (updates.assigneeId != null) {
      task.assigneeId = updates.assigneeId;
    }

    return task;
  }

  @Transactional
  public boolean deleteTask(Long taskId) {
    return Task.deleteById(taskId);
  }

  @Transactional
  public Task assignTask(Long taskId, Long userId) {
    Task task = Task.findById(taskId);
    if (task == null) {
      throw new NotFoundException("Task not found");
    }

    Long previousAssignee = task.assigneeId;
    task.assigneeId = userId;

    // Publish events
    eventPublisher.publishActivity(
        ActivityEventFactory.taskAssigned(1L, task.projectId, taskId, userId, task.title)
    );

    if (userId != null) {
      eventPublisher.publishNotification(
          NotificationEventFactory.taskAssigned(1L, userId, task.projectId, taskId, task.title)
      );
    }

    return task;
  }

  @Transactional
  public Task transitionTask(Long taskId, TaskStatus newStatus) {
    Task task = Task.findById(taskId);
    if (task == null) {
      throw new NotFoundException("Task not found");
    }

    TaskStatus oldStatus = task.status;
    task.status = newStatus;

    // Publish activity event
    eventPublisher.publishActivity(
        ActivityEventFactory.taskStatusChanged(1L, task.projectId, taskId,
            oldStatus.name(), newStatus.name(), task.title)
    );

    // Notify assignee if task status changed
    if (task.assigneeId != null) {
      eventPublisher.publishNotification(
          NotificationEventFactory.taskStatusChanged(1L, task.assigneeId,
              task.projectId, taskId, task.title, newStatus.name())
      );
    }

    return task;
  }
}