package org.taskmanagementapp.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.service.SearchService;

@Path("/search")
@Tag(name = "Search", description = "Search operations")
@Produces(MediaType.APPLICATION_JSON)
public class SearchController {

  @Inject
  SearchService searchService;

  @GET
  @Operation(summary = "Search tasks")
  public Response searchTasks(
      @Parameter(description = "Search query text") @QueryParam("q") String query,
      @Parameter(description = "Filter by project ID") @QueryParam("projectId") Long projectId,
      @Parameter(description = "Filter by assignee ID") @QueryParam("assigneeId") Long assigneeId) {

    List<Task> tasks = searchService.searchTasks(query, projectId, assigneeId);
    return Response.ok(tasks).build();
  }
}