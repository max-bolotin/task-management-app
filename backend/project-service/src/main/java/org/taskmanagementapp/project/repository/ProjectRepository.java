package org.taskmanagementapp.project.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import org.taskmanagementapp.project.entity.Project;

@ApplicationScoped
public class ProjectRepository implements PanacheRepository<Project> {

  public Optional<Project> findByKey(String key) {
    return find("key", key).firstResultOptional();
  }

  public boolean existsByKey(String key) {
    return count("key", key) > 0;
  }
}