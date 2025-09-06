package org.taskmanagementapp.project.service;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.project.entity.Project;
import org.taskmanagementapp.project.entity.Task;

@Path("/projects/{projectId}/tasks")
@Tag(name = "Tasks", description = "Task management operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

  @GET
  @Operation(summary = "Get all tasks for a project")
  public Response getProjectTasks(@PathParam("projectId") Long projectId) {
    if (Project.findById(projectId) == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    List<Task> tasks = Task.find("projectId", projectId).list();
    return Response.ok(tasks).build();
  }

  @POST
  @Transactional
  @Operation(summary = "Create new task in project")
  public Response createTask(@PathParam("projectId") Long projectId, Task task) {
    if (Project.findById(projectId) == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Project not found")
          .build();
    }

    if (task.title == null || task.title.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Task title is required")
          .build();
    }

    task.id = null; // Ensure ID is auto-generated
    task.projectId = projectId;
    task.persist();
    return Response.status(Response.Status.CREATED).entity(task).build();
  }
}