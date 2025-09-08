package org.taskmanagementapp.activity.repo

import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.gte
import org.litote.kmongo.reactivestreams.KMongo
import org.taskmanagementapp.activity.model.ActivityEvent
import org.taskmanagementapp.activity.model.FailedEvent

open class ActivityRepository(
    mongoUri: String,
    dbName: String,
    collection: String
) {
    private val client = KMongo.createClient(mongoUri).coroutine
    private val database = client.getDatabase(dbName)
    private val events = database.getCollection<ActivityEvent>(collection)
    private val failedEvents = database.getCollection<FailedEvent>("failed_events")

    init {
        runBlocking {
            events.createIndex(
                Indexes.compoundIndex(
                    Indexes.ascending(ActivityEvent::type.name),
                    Indexes.ascending(ActivityEvent::projectId.name),
                    Indexes.ascending(ActivityEvent::taskId.name),
                    Indexes.ascending(ActivityEvent::userId.name),
                    Indexes.descending(ActivityEvent::timestamp.name)
                ),
                IndexOptions().background(true)
            )
        }
    }

    suspend fun save(event: ActivityEvent): String {
        val existing = events.findOne(
            and(
                ActivityEvent::type eq event.type,
                ActivityEvent::projectId eq event.projectId,
                ActivityEvent::taskId eq event.taskId,
                ActivityEvent::userId eq event.userId,
                ActivityEvent::timestamp gte event.timestamp.minusSeconds(5)
            )
        )
        if (existing != null) {
            return existing.id.toHexString() // skip saving duplicate
        }
        val res = events.insertOne(event)
        return (res.insertedId?.asObjectId()?.value ?: event.id).toHexString()
    }

    suspend fun byProject(projectId: String): List<ActivityEvent> =
        events.find(ActivityEvent::projectId eq projectId)
            .sort(descending(ActivityEvent::timestamp))
            .toFlow().toList()

    suspend fun byTask(taskId: String): List<ActivityEvent> =
        events.find(ActivityEvent::taskId eq taskId)
            .sort(descending(ActivityEvent::timestamp))
            .toFlow().toList()

    suspend fun get(id: String): ActivityEvent? =
        events.findOneById(ObjectId(id))

    suspend fun all(): List<ActivityEvent> =
        events.find()
            .sort(descending(ActivityEvent::timestamp))
            .toFlow().toList()

    suspend fun delete(id: String): Boolean =
        events.deleteOneById(ObjectId(id)).wasAcknowledged()

    suspend fun byProjectAndTask(projectId: String, taskId: String): List<ActivityEvent> =
        events.find(ActivityEvent::projectId eq projectId, ActivityEvent::taskId eq taskId)
            .sort(descending(ActivityEvent::timestamp))
            .toFlow().toList()

    suspend fun saveFailedEvent(failedEvent: FailedEvent): String {
        val res = failedEvents.insertOne(failedEvent)
        return (res.insertedId?.asObjectId()?.value ?: failedEvent.id).toHexString()
    }

    suspend fun getFailedEvents(): List<FailedEvent> =
        failedEvents.find().toFlow().toList()
}
