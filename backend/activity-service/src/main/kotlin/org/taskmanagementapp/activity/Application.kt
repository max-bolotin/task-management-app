package org.taskmanagementapp.activity

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.bson.types.ObjectId
import org.taskmanagementapp.activity.consumer.ActivityEventConsumer
import org.taskmanagementapp.activity.repo.ActivityRepository
import org.taskmanagementapp.activity.routes.activityRoutes
import org.taskmanagementapp.activity.routes.healthRoutes
import org.taskmanagementapp.activity.service.ActivityService

fun main() {
    val server = embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toIntOrNull() ?: 8084,
        host = "0.0.0.0",
        module = Application::module
    )

    Runtime.getRuntime().addShutdownHook(Thread {
        server.stop(1000, 5000)
    })

    server.start(wait = true)
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

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        anyHost()
    }

    install(SwaggerUI) {
        swagger {
            swaggerUrl = "swagger-ui"
            forwardRoot = true
        }
        info {
            title = "Activity Service API"
            version = "1.0.0"
            description = "Activity tracking service with auto-generated documentation"
        }
        server {
            url = "http://localhost:8084"
            description = "Development server"
        }
    }

    val mongoUri = environment.config.propertyOrNull("mongo.uri")?.getString()
        ?: System.getenv("MONGO_URI") ?: "mongodb://localhost:27017"
    val dbName = environment.config.propertyOrNull("mongo.database")?.getString()
        ?: System.getenv("MONGO_DB") ?: "activitydb"
    val coll = environment.config.propertyOrNull("mongo.collection")?.getString()
        ?: System.getenv("MONGO_COLLECTION") ?: "events"

    val repo = ActivityRepository(mongoUri, dbName, coll)
    val service = ActivityService(repo)

    // Kafka configuration
    val kafkaConfig = mapOf(
        "bootstrap.servers" to (environment.config.propertyOrNull("kafka.bootstrap.servers")
            ?.getString()
            ?: System.getenv("KAFKA_BOOTSTRAP_SERVERS") ?: "localhost:9092"),
        "group.id" to (environment.config.propertyOrNull("kafka.group.id")?.getString()
            ?: System.getenv("KAFKA_GROUP_ID") ?: "activity-service"),
        "topic" to (environment.config.propertyOrNull("kafka.topic")?.getString()
            ?: System.getenv("KAFKA_TOPIC") ?: "activity-events"),
        "auto.offset.reset" to (environment.config.propertyOrNull("kafka.auto.offset.reset")
            ?.getString()
            ?: System.getenv("KAFKA_AUTO_OFFSET_RESET") ?: "earliest")
    )

    // Start Kafka consumer
    val objectMapper = com.fasterxml.jackson.databind.ObjectMapper().apply {
        registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
        registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
    val consumer = ActivityEventConsumer(repo, kafkaConfig, objectMapper)
    consumer.start()

    routing {
        healthRoutes()
        activityRoutes(service)
    }
}
