package org.taskmanagementapp.activity.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class ActivityEvent(
    @BsonId val id: ObjectId = ObjectId(),
    val type: String,
    val projectId: String,
    val taskId: String? = null,
    val userId: String? = null,
    val timestamp: Instant = Instant.now(),
    val payload: Map<String, Any?> = emptyMap()
)

data class CreateEventRequest(
    val type: String,
    val projectId: String,
    val taskId: String? = null,
    val userId: String? = null,
    val payload: Map<String, Any?> = emptyMap()
)

data class CreateEventResponse(val id: String)
