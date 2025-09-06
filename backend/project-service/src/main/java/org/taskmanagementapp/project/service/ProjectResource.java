package org.taskmanagementapp.project.service;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.project.entity.Project;

@Path("/projects")
@Tag(name = "Projects", description = "Project management operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectResource {

  @GET
  @Operation(summary = "Get all projects")
  public List<Project> getAllProjects() {
    return Project.listAll();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Get project by ID")
  public Response getProject(@PathParam("id") Long id) {
    Project project = Project.findById(id);
    return project != null ? Response.ok(project).build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }

  @POST
  @Transactional
  @Operation(summary = "Create new project")
  public Response createProject(Project project) {
    // Validate required fields
    if (project.name == null || project.name.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Project name is required")
          .build();
    }

    if (project.key == null || project.key.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Project key is required")
          .build();
    }

    // TODO: Auto-assign owner from JWT token when authentication is implemented
    // In Jira, the logged-in user automatically becomes the project owner
    // For now, require explicit ownerId until we have JWT/Security context
    if (project.ownerId == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Owner ID is required")
          .build();
    }

    // Check if key already exists
    if (Project.find("key", project.key).firstResultOptional().isPresent()) {
      return Response.status(Response.Status.CONFLICT)
          .entity("Project key already exists")
          .build();
    }

    project.id = null; // Ensure ID is auto-generated
    project.persist();
    return Response.status(Response.Status.CREATED).entity(project).build();
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  @Operation(summary = "Delete project")
  public Response deleteProject(@PathParam("id") Long id) {
    boolean deleted = Project.deleteById(id);
    return deleted ? Response.noContent().build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }
}