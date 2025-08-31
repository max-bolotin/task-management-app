package org.taskmanagementapp.activity

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.bson.types.ObjectId
import org.taskmanagementapp.activity.repo.ActivityRepository
import org.taskmanagementapp.activity.routes.activityRoutes

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toIntOrNull() ?: 8084,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            registerModule(KotlinModule.Builder().build())
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            addMixIn(ObjectId::class.java, ObjectIdMixin::class.java)
        }
    }

    val mongoUri = environment.config.propertyOrNull("mongo.uri")?.getString()
        ?: System.getenv("MONGO_URI") ?: "mongodb://localhost:27017"
    val dbName = environment.config.propertyOrNull("mongo.database")?.getString()
        ?: System.getenv("MONGO_DB") ?: "activitydb"
    val coll = environment.config.propertyOrNull("mongo.collection")?.getString()
        ?: System.getenv("MONGO_COLLECTION") ?: "events"

    val repo = ActivityRepository(mongoUri, dbName, coll)
    activityRoutes(repo)
}
