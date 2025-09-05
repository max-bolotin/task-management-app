package org.taskmanagementapp.project.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.taskmanagementapp.common.dto.ProjectDto;
import org.taskmanagementapp.project.entity.Project;

@ApplicationScoped
public class ProjectToProjectDtoConverter {

  public ProjectDto convert(Project project) {
    return new ProjectDto(
        project.id,
        project.name,
        project.key,
        project.description,
        project.ownerId,
        project.createdAt,
        project.updatedAt
    );
  }
}