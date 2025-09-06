package org.taskmanagementapp.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.exception.ValidationException;
import org.taskmanagementapp.project.service.TaskService;

@Path("/projects/{projectId}/tasks")
@Tag(name = "Tasks", description = "Task management operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectTaskController {

  @Inject
  TaskService taskService;

  @GET
  @Operation(summary = "Get all tasks for a project")
  public Response getProjectTasks(@PathParam("projectId") Long projectId) {
    try {
      List<Task> tasks = taskService.getTasksByProject(projectId);
      return Response.ok(tasks).build();
    } catch (NotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @POST
  @Operation(summary = "Create new task in project")
  public Response createTask(@PathParam("projectId") Long projectId, Task task) {
    try {
      Task createdTask = taskService.createTask(projectId, task);
      return Response.status(Response.Status.CREATED).entity(createdTask).build();
    } catch (NotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
    } catch (ValidationException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
  }
}