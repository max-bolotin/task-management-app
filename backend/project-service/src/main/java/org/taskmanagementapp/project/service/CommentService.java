package org.taskmanagementapp.project.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import org.taskmanagementapp.project.entity.Comment;
import org.taskmanagementapp.project.entity.Task;
import org.taskmanagementapp.project.exception.NotFoundException;
import org.taskmanagementapp.project.exception.ValidationException;

@ApplicationScoped
public class CommentService {

  public List<Comment> getCommentsByTask(Long taskId) {
    if (Task.findById(taskId) == null) {
      throw new NotFoundException("Task not found");
    }
    return Comment.find("taskId", taskId).list();
  }

  @Transactional
  public Comment createComment(Long taskId, Comment comment) {
    if (Task.findById(taskId) == null) {
      throw new NotFoundException("Task not found");
    }

    if (comment.content == null || comment.content.trim().isEmpty()) {
      throw new ValidationException("Comment content is required");
    }

    comment.id = null;
    comment.taskId = taskId;
    comment.persist();
    return comment;
  }

  @Transactional
  public boolean deleteComment(Long commentId) {
    return Comment.deleteById(commentId);
  }
}