package org.taskmanagementapp.project.service;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.project.entity.Task;

@Path("/tasks")
@Tag(name = "Tasks", description = "Direct task operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskDirectResource {

  @GET
  @Path("/{taskId}")
  @Operation(summary = "Get task by ID")
  public Response getTask(@PathParam("taskId") Long taskId) {
    Task task = Task.findById(taskId);
    return task != null ? Response.ok(task).build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }

  @PATCH
  @Path("/{taskId}")
  @Transactional
  @Operation(summary = "Update task")
  public Response updateTask(@PathParam("taskId") Long taskId, Task updates) {
    Task task = Task.findById(taskId);
    if (task == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
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

    return Response.ok(task).build();
  }

  @DELETE
  @Path("/{taskId}")
  @Transactional
  @Operation(summary = "Delete task")
  public Response deleteTask(@PathParam("taskId") Long taskId) {
    boolean deleted = Task.deleteById(taskId);
    return deleted ? Response.noContent().build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }

  @POST
  @Path("/{taskId}/assign/{userId}")
  @Transactional
  @Operation(summary = "Assign task to user")
  public Response assignTask(@PathParam("taskId") Long taskId, @PathParam("userId") Long userId) {
    Task task = Task.findById(taskId);
    if (task == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    task.assigneeId = userId;
    return Response.ok(task).build();
  }
}