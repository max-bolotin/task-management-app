package org.taskmanagementapp.activity

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
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
    install(ContentNegotiation) { jackson() }

    val mongoUri = environment.config.propertyOrNull("mongo.uri")?.getString()
        ?: System.getenv("MONGO_URI") ?: "mongodb://mongo:27017"
    val dbName = environment.config.propertyOrNull("mongo.database")?.getString()
        ?: System.getenv("MONGO_DB") ?: "activitydb"
    val coll = environment.config.propertyOrNull("mongo.collection")?.getString()
        ?: System.getenv("MONGO_COLLECTION") ?: "events"

    val repo = ActivityRepository(mongoUri, dbName, coll)
    activityRoutes(repo)
}
