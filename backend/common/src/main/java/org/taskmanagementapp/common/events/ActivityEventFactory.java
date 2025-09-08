package org.taskmanagementapp.common.events;

import java.util.Map;
import org.taskmanagementapp.common.dto.ActivityEvent;
import org.taskmanagementapp.common.enums.ActivityEventType;

public class ActivityEventFactory {

  public static ActivityEvent projectCreated(Long userId, Long projectId, String projectName) {
    return new ActivityEvent(
        ActivityEventType.PROJECT_CREATED,
        userId,
        projectId,
        null,
        null,
        "Created project: " + projectName,
        Map.of("projectName", projectName),
        null
    );
  }

  public static ActivityEvent taskCreated(Long userId, Long projectId, Long taskId,
      String taskTitle) {
    return new ActivityEvent(
        ActivityEventType.TASK_CREATED,
        userId,
        projectId,
        taskId,
        null,
        "Created task: " + taskTitle,
        Map.of("taskTitle", taskTitle),
        null
    );
  }

  public static ActivityEvent taskAssigned(Long userId, Long projectId, Long taskId,
      Long assigneeId, String taskTitle) {
    return new ActivityEvent(
        ActivityEventType.TASK_ASSIGNED,
        userId,
        projectId,
        taskId,
        null,
        "Assigned task: " + taskTitle,
        Map.of("taskTitle", taskTitle, "assigneeId", assigneeId),
        null
    );
  }

  public static ActivityEvent taskStatusChanged(Long userId, Long projectId, Long taskId,
      String oldStatus, String newStatus, String taskTitle) {
    return new ActivityEvent(
        ActivityEventType.TASK_STATUS_CHANGED,
        userId,
        projectId,
        taskId,
        null,
        "Changed task status from " + oldStatus + " to " + newStatus + ": " + taskTitle,
        Map.of("taskTitle", taskTitle, "oldStatus", oldStatus, "newStatus", newStatus),
        null
    );
  }

  public static ActivityEvent commentCreated(Long userId, Long projectId, Long taskId,
      Long commentId, String taskTitle) {
    return new ActivityEvent(
        ActivityEventType.COMMENT_CREATED,
        userId,
        projectId,
        taskId,
        commentId,
        "Commented on task: " + taskTitle,
        Map.of("taskTitle", taskTitle),
        null
    );
  }
}