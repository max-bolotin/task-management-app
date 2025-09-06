package org.taskmanagementapp.project.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment extends PanacheEntity {

  @Column(nullable = false)
  public String content;

  @Column(name = "task_id", nullable = false)
  public Long taskId;

  @Column(name = "author_id", nullable = false)
  public Long authorId;

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}