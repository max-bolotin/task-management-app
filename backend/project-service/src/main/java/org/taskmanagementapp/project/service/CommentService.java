package org.taskmanagementapp.project.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.taskmanagementapp.common.events.ActivityEventFactory;
import org.taskmanagementapp.common.events.NotificationEventFactory;
import org.taskmanagementapp.project.entity.Comment;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.exception.ValidationException;

@ApplicationScoped
public class CommentService {

  @Inject
  EventPublisher eventPublisher;

  public List<Comment> getCommentsByTask(Long taskId) {
    if (Task.findById(taskId) == null) {
      throw new NotFoundException("Task not found");
    }
    return Comment.find("taskId", taskId).list();
  }

  @Transactional
  public Comment createComment(Long taskId, Comment comment) {
    Task task = Task.findById(taskId);
    if (task == null) {
      throw new NotFoundException("Task not found");
    }

    if (comment.content == null || comment.content.trim().isEmpty()) {
      throw new ValidationException("Comment content is required");
    }

    comment.id = null;
    comment.taskId = taskId;
    comment.persist();

    // Publish events
    eventPublisher.publishActivity(
        ActivityEventFactory.commentCreated(comment.authorId, task.projectId, taskId, comment.id, task.title)
    );

    // Notify task assignee if different from comment author
    if (task.assigneeId != null && !task.assigneeId.equals(comment.authorId)) {
      eventPublisher.publishNotification(
          NotificationEventFactory.taskCommented(comment.authorId, task.assigneeId,
              task.projectId, taskId, comment.id, task.title)
      );
    }

    return comment;
  }

  @Transactional
  public boolean deleteComment(Long commentId) {
    return Comment.deleteById(commentId);
  }
}