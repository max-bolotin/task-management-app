package org.taskmanagementapp.project.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.common.dto.ProjectDto;
import org.taskmanagementapp.project.converter.ProjectDtoToProjectConverter;
import org.taskmanagementapp.project.converter.ProjectToProjectDtoConverter;
import org.taskmanagementapp.project.repository.ProjectRepository;

@Path("/projects")
@Tag(name = "Projects", description = "Project management operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectResource {

  @Inject
  ProjectRepository projectRepository;

  @Inject
  ProjectToProjectDtoConverter toDto;

  @Inject
  ProjectDtoToProjectConverter toEntity;

  @GET
  @Operation(summary = "Get all projects")
  public List<ProjectDto> getAllProjects() {
    return projectRepository.listAll()
        .stream()
        .map(toDto::convert)
        .toList();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Get project by ID")
  public Response getProject(@PathParam("id") Long id) {
    return projectRepository.findByIdOptional(id)
        .map(toDto::convert)
        .map(dto -> Response.ok(dto).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @POST
  @Transactional
  @Operation(summary = "Create new project")
  public Response createProject(@Valid ProjectDto projectDto) {
    if (projectRepository.existsByKey(projectDto.key())) {
      return Response.status(Response.Status.CONFLICT)
          .entity("Project key already exists")
          .build();
    }

    var project = toEntity.convert(projectDto);
    projectRepository.persist(project);
    return Response.status(Response.Status.CREATED)
        .entity(toDto.convert(project))
        .build();
  }

  @PATCH
  @Path("/{id}")
  @Transactional
  @Operation(summary = "Update project")
  public Response updateProject(@PathParam("id") Long id, @Valid ProjectDto projectDto) {
    return projectRepository.findByIdOptional(id)
        .map(project -> {
          toEntity.updateEntity(project, projectDto);
          return Response.ok(toDto.convert(project)).build();
        })
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  @Operation(summary = "Delete project")
  public Response deleteProject(@PathParam("id") Long id) {
    boolean deleted = projectRepository.deleteById(id);
    return deleted ? Response.noContent().build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }
}
