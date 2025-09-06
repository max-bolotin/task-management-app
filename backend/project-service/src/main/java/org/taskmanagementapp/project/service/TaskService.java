package org.taskmanagementapp.project.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import org.taskmanagementapp.common.enums.TaskStatus;
import org.taskmanagementapp.project.entity.Project;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.exception.ValidationException;

@ApplicationScoped
public class TaskService {

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

    task.assigneeId = userId;
    return task;
  }

  @Transactional
  public Task transitionTask(Long taskId, TaskStatus newStatus) {
    Task task = Task.findById(taskId);
    if (task == null) {
      throw new NotFoundException("Task not found");
    }

    task.status = newStatus;
    return task;
  }
}