package org.taskmanagementapp.activity.service

import org.taskmanagementapp.activity.model.ActivityEvent
import org.taskmanagementapp.activity.model.CreateEventRequest
import org.taskmanagementapp.activity.model.FailedEvent
import org.taskmanagementapp.activity.repo.ActivityRepository

open class ActivityService(private val repository: ActivityRepository) {

    suspend fun createEvent(request: CreateEventRequest): String {
        val event = ActivityEvent(
            type = request.type,
            projectId = request.projectId,
            taskId = request.taskId,
            userId = request.userId,
            payload = request.payload
        )
        return repository.save(event)
    }

    suspend fun getEvents(projectId: String?, taskId: String?): List<ActivityEvent> {
        return when {
            projectId != null && taskId != null -> repository.byProjectAndTask(projectId, taskId)
            projectId != null -> repository.byProject(projectId)
            taskId != null -> repository.byTask(taskId)
            else -> throw IllegalArgumentException("Provide projectId or taskId")
        }
    }

    suspend fun getEventById(id: String): ActivityEvent? {
        return repository.get(id)
    }

    suspend fun deleteEvent(id: String): Boolean {
        return repository.delete(id)
    }

    suspend fun getAllEvents(): List<ActivityEvent> {
        return repository.all()
    }

    suspend fun getFailedEvents(): List<FailedEvent> {
        return repository.getFailedEvents()
    }
}