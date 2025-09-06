package org.taskmanagementapp.project.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import org.taskmanagementapp.project.entity.Project;
import org.taskmanagementapp.project.exception.ConflictException;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.exception.ValidationException;

@ApplicationScoped
public class ProjectService {

  public List<Project> getAllProjects() {
    return Project.listAll();
  }

  public Project getProjectById(Long id) {
    Project project = Project.findById(id);
    if (project == null) {
      throw new NotFoundException("Project not found");
    }
    return project;
  }

  @Transactional
  public Project createProject(Project project) {
    validateProject(project);

    // Check if key already exists
    if (Project.find("key", project.key).firstResultOptional().isPresent()) {
      throw new ConflictException("Project key already exists");
    }

    project.id = null; // Ensure ID is auto-generated
    project.persist();
    return project;
  }

  @Transactional
  public void deleteProject(Long id) {
    if (!Project.deleteById(id)) {
      throw new NotFoundException("Project not found");
    }
  }

  private void validateProject(Project project) {
    if (project.name == null || project.name.trim().isEmpty()) {
      throw new ValidationException("Project name is required");
    }
    if (project.key == null || project.key.trim().isEmpty()) {
      throw new ValidationException("Project key is required");
    }
    // TODO: Auto-assign owner from JWT token when authentication is implemented
    // In Jira, the logged-in user automatically becomes the project owner
    // For now, require explicit ownerId until we have JWT/Security context
    if (project.ownerId == null) {
      throw new ValidationException("Owner ID is required");
    }
  }
}