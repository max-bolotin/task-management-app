package org.taskmanagementapp.project.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;
import org.taskmanagementapp.project.entity.Task;

@ApplicationScoped
public class SearchService {

  public List<Task> searchTasks(String query, Long projectId, Long assigneeId) {
    List<Task> tasks = Task.listAll();

    return tasks.stream()
        .filter(task -> query == null || query.trim().isEmpty() ||
            task.title.toLowerCase().contains(query.toLowerCase()) ||
            (task.description != null && task.description.toLowerCase()
                .contains(query.toLowerCase())))
        .filter(task -> projectId == null || task.projectId.equals(projectId))
        .filter(task -> assigneeId == null || (task.assigneeId != null && task.assigneeId.equals(
            assigneeId)))
        .collect(Collectors.toList());
  }
}