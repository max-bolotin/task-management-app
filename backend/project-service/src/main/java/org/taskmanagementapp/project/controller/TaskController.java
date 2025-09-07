package org.taskmanagementapp.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.common.enums.TaskStatus;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.service.TaskService;

@Path("/tasks")
@Tag(name = "Tasks", description = "Direct task operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController {

  @Inject
  TaskService taskService;

  @GET
  @Path("/{taskId}")
  @Operation(summary = "Get task by ID")
  public Response getTask(@PathParam("taskId") Long taskId) {
    try {
      Task task = taskService.getTaskById(taskId);
      return Response.ok(task).build();
    } catch (NotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @PATCH
  @Path("/{taskId}")
  @Operation(summary = "Update task")
  public Response updateTask(@PathParam("taskId") Long taskId, Task updates) {
    try {
      Task task = taskService.updateTask(taskId, updates);
      return Response.ok(task).build();
    } catch (NotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @DELETE
  @Path("/{taskId}")
  @Operation(summary = "Delete task")
  public Response deleteTask(@PathParam("taskId") Long taskId) {
    boolean deleted = taskService.deleteTask(taskId);
    return deleted ? Response.noContent().build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }

  @POST
  @Path("/{taskId}/assign/{userId}")
  @Consumes(MediaType.WILDCARD)
  @Operation(summary = "Assign task to user")
  public Response assignTask(@PathParam("taskId") Long taskId, @PathParam("userId") Long userId) {
    try {
      Task task = taskService.assignTask(taskId, userId);
      return Response.ok(task).build();
    } catch (NotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @POST
  @Path("/{taskId}/transition")
  @Operation(summary = "Transition task status")
  public Response transitionTask(@PathParam("taskId") Long taskId, TransitionRequest request) {
    Task task = taskService.transitionTask(taskId, request.status);
    return Response.ok(task).build();
  }

  public static class TransitionRequest {

    public TaskStatus status;
  }
}