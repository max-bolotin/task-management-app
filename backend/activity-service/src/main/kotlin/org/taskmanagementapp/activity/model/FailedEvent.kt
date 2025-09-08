package org.taskmanagementapp.activity.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class FailedEvent(
    @BsonId val id: ObjectId = ObjectId(),
    val originalEvent: String,
    val errorMessage: String,
    val timestamp: Instant = Instant.now(),
    val retryCount: Int = 0
)