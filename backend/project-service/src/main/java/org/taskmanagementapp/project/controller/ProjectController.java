package org.taskmanagementapp.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.project.entity.Project;
import org.taskmanagementapp.project.service.ProjectService;

@Path("/projects")
@Tag(name = "Projects", description = "Project management operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectController {

  @Inject
  ProjectService projectService;

  @GET
  @Operation(summary = "Get all projects")
  public List<Project> getAllProjects() {
    return projectService.getAllProjects();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Get project by ID")
  public Project getProject(@PathParam("id") Long id) {
    return projectService.getProjectById(id);
  }

  @POST
  @Operation(summary = "Create new project")
  public Response createProject(Project project) {
    Project created = projectService.createProject(project);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Delete project")
  public Response deleteProject(@PathParam("id") Long id) {
    projectService.deleteProject(id);
    return Response.noContent().build();
  }
}