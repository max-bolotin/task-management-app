package org.taskmanagementapp.notification.service.repository;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
import org.taskmanagementapp.notification.service.entity.Notification;

import java.util.List;

@MongoRepository
public interface NotificationRepository extends CrudRepository<Notification, String> {

  List<Notification> findByUserId(Long userId);

  List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}