package org.taskmanagementapp.activity.repo

import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.taskmanagementapp.activity.model.ActivityEvent

class ActivityRepository(
    mongoUri: String,
    dbName: String,
    collection: String
) {
    private val client = KMongo.createClient(mongoUri).coroutine
    private val database = client.getDatabase(dbName)
    private val events = database.getCollection<ActivityEvent>(collection)

    suspend fun save(event: ActivityEvent): String {
        val res = events.insertOne(event)
        return (res.insertedId?.asObjectId()?.value ?: event.id).toHexString()
    }

    suspend fun byProject(projectId: String): List<ActivityEvent> =
        events.find(ActivityEvent::projectId eq projectId).toFlow().toList()

    suspend fun byTask(taskId: String): List<ActivityEvent> =
        events.find(ActivityEvent::taskId eq taskId).toFlow().toList()

    suspend fun get(id: String): ActivityEvent? =
        events.findOneById(ObjectId(id))
}
