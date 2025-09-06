package org.taskmanagementapp.project.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project extends PanacheEntity {

  @Column(nullable = false)
  public String name;

  @Column(nullable = false, unique = true)
  public String key;

  public String description;

  @Column(name = "owner_id", nullable = false)
  public Long ownerId;

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