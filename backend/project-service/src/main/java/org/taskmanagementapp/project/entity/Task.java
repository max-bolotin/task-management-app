package org.taskmanagementapp.project.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.taskmanagementapp.common.enums.TaskStatus;

@Entity
@Table(name = "tasks")
public class Task extends PanacheEntity {

  @Column(nullable = false)
  public String title;

  public String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  public TaskStatus status = TaskStatus.TODO;

  @Column(name = "project_id", nullable = false)
  public Long projectId;

  @Column(name = "assignee_id")
  public Long assigneeId;

  @Column(name = "reporter_id")
  public Long reporterId;

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt;

  @Column(name = "updated_at")
  public LocalDateTime updatedAt;

  @PrePersist
  void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}