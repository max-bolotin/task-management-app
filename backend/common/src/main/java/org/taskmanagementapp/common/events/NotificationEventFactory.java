package org.taskmanagementapp.common.events;

import java.util.Map;
import org.taskmanagementapp.common.dto.NotificationEvent;
import org.taskmanagementapp.common.enums.NotificationEventType;

public class NotificationEventFactory {

  public static NotificationEvent taskAssigned(Long actorUserId, Long targetUserId, Long projectId,
      Long taskId, String taskTitle) {
    return new NotificationEvent(
        NotificationEventType.TASK_ASSIGNED,
        targetUserId,
        actorUserId,
        projectId,
        taskId,
        null,
        "You have been assigned to task: " + taskTitle,
        Map.of("taskTitle", taskTitle),
        null
    );
  }

  public static NotificationEvent taskCommented(Long actorUserId, Long targetUserId, Long projectId,
      Long taskId, Long commentId, String taskTitle) {
    return new NotificationEvent(
        NotificationEventType.TASK_COMMENTED,
        targetUserId,
        actorUserId,
        projectId,
        taskId,
        commentId,
        "New comment on task: " + taskTitle,
        Map.of("taskTitle", taskTitle),
        null
    );
  }

  public static NotificationEvent taskStatusChanged(Long actorUserId, Long targetUserId,
      Long projectId, Long taskId, String taskTitle, String newStatus) {
    return new NotificationEvent(
        NotificationEventType.TASK_STATUS_CHANGED,
        targetUserId,
        actorUserId,
        projectId,
        taskId,
        null,
        "Task status changed to " + newStatus + ": " + taskTitle,
        Map.of("taskTitle", taskTitle, "newStatus", newStatus),
        null
    );
  }

  public static NotificationEvent mention(Long actorUserId, Long targetUserId, Long projectId,
      Long taskId, Long commentId, String taskTitle) {
    return new NotificationEvent(
        NotificationEventType.MENTION,
        targetUserId,
        actorUserId,
        projectId,
        taskId,
        commentId,
        "You were mentioned in task: " + taskTitle,
        Map.of("taskTitle", taskTitle),
        null
    );
  }
}