package org.taskmanagementapp.project.converter;

import jakarta.enterprise.context.ApplicationScoped;
import org.taskmanagementapp.common.dto.ProjectDto;
import org.taskmanagementapp.project.entity.Project;

@ApplicationScoped
public class ProjectDtoToProjectConverter {

  public Project convert(ProjectDto dto) {
    var project = new Project();
    project.name = dto.name();
    project.key = dto.key();
    project.description = dto.description();
    project.ownerId = dto.ownerId();
    return project;
  }

  public void updateEntity(Project project, ProjectDto dto) {
    if (dto.name() != null) {
      project.name = dto.name();
    }
    if (dto.description() != null) {
      project.description = dto.description();
    }
  }
}