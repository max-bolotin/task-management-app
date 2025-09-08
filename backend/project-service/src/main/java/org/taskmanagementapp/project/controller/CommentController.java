package org.taskmanagementapp.project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.taskmanagementapp.project.entity.Comment;
import org.taskmanagementapp.project.service.CommentService;

@Path("/tasks/{taskId}/comments")
@Tag(name = "Comments", description = "Task comment operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentController {

  @Inject
  CommentService commentService;

  @GET
  @Operation(summary = "Get all comments for a task")
  public Response getTaskComments(
      @Parameter(description = "Task ID") @PathParam("taskId") Long taskId) {
    List<Comment> comments = commentService.getCommentsByTask(taskId);
    return Response.ok(comments).build();
  }

  @POST
  @Operation(summary = "Create new comment on task")
  public Response createComment(
      @Parameter(description = "Task ID") @PathParam("taskId") Long taskId, Comment comment) {
    Comment createdComment = commentService.createComment(taskId, comment);
    return Response.status(Response.Status.CREATED).entity(createdComment).build();
  }

  @DELETE
  @Path("/{commentId}")
  @Operation(summary = "Delete comment")
  public Response deleteComment(
      @Parameter(description = "Comment ID") @PathParam("commentId") Long commentId) {
    boolean deleted = commentService.deleteComment(commentId);
    return deleted ? Response.noContent().build()
        : Response.status(Response.Status.NOT_FOUND).build();
  }
}