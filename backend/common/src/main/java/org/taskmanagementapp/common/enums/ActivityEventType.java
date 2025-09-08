package org.taskmanagementapp.common.enums;

public enum ActivityEventType {
  // Project events
  PROJECT_CREATED,
  PROJECT_UPDATED,
  PROJECT_DELETED,

  // Task events
  TASK_CREATED,
  TASK_UPDATED,
  TASK_DELETED,
  TASK_ASSIGNED,
  TASK_STATUS_CHANGED,

  // Comment events
  COMMENT_CREATED,
  COMMENT_UPDATED,
  COMMENT_DELETED
}