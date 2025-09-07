package org.taskmanagementapp.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.common.enums.TaskStatus;
import org.taskmanagementapp.common.events.ActivityEventFactory;
import org.taskmanagementapp.common.events.NotificationEventFactory;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.service.EventPublisher;
import org.taskmanagementapp.project.service.TaskService;

@Path("/tasks")
@Tag(name = "Tasks", description = "Direct task operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskController {

  @Inject
  TaskService taskService;

  @Inject
  EventPublisher eventPublisher;

  @GET
  @Path("/{taskId}")
  @Operation(summary = "Get task by ID")
  public Response getTask(@Parameter(description = "Task ID") @PathParam("taskId") Long taskId) {
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
  public Response updateTask(@Parameter(description = "Task ID") @PathParam("taskId") Long taskId,
      Task updates) {
    try {
      Task originalTask = taskService.getTaskById(taskId);
      TaskStatus oldStatus = originalTask.status;

      Task task = taskService.updateTask(taskId, updates);

      // Publish events after transaction completes
      //fixme: move the following publishing method from controller to another level
      if (updates.status != null && oldStatus != updates.status) {
        try {
          eventPublisher.publishActivity(
              ActivityEventFactory.taskStatusChanged(1L, task.projectId, taskId,
                  oldStatus.name(), updates.status.name(), task.title)
          );

          if (task.assigneeId != null) {
            eventPublisher.publishNotification(
                NotificationEventFactory.taskStatusChanged(1L, task.assigneeId,
                    task.projectId, taskId, task.title, updates.status.name())
            );
          }
        } catch (Exception e) {
          // Ignore event publishing errors
        }
      }

      return Response.ok(task).build();
    } catch (NotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @DELETE
  @Path("/{taskId}")
  @Operation(summary = "Delete task")
  public Response deleteTask(@Parameter(description = "Task ID") @PathParam("taskId") Long taskId) {
    boolean deleted = taskService.deleteTask(taskId);
    return deleted ? Response.noContent().build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }

  @POST
  @Path("/{taskId}/assign/{userId}")
  @Consumes(MediaType.WILDCARD)
  @Operation(summary = "Assign task to user")
  public Response assignTask(@Parameter(description = "Task ID") @PathParam("taskId") Long taskId,
      @Parameter(description = "User ID") @PathParam("userId") Long userId) {
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
  public Response transitionTask(
      @Parameter(description = "Task ID") @PathParam("taskId") Long taskId,
      TransitionRequest request) {
    Task task = taskService.transitionTask(taskId, request.status);
    return Response.ok(task).build();
  }

  public static class TransitionRequest {

    public TaskStatus status;
  }
}