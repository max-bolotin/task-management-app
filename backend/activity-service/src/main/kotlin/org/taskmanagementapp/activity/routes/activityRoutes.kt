package org.taskmanagementapp.activity.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.taskmanagementapp.activity.model.ActivityEvent
import org.taskmanagementapp.activity.model.CreateEventRequest
import org.taskmanagementapp.activity.model.CreateEventResponse
import org.taskmanagementapp.activity.repo.ActivityRepository

fun Route.activityRoutes(repo: ActivityRepository) {
    get("/ping") {
        call.respondText("Activity Service (Kotlin/Ktor) is up!")
    }

    get("/health") {
        call.respondText("Activity Service (Kotlin/Ktor) is healthy!")
    }

    get("/all") {
        try {
            val allEntries = repo.all()
            call.respond(HttpStatusCode.OK, allEntries)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
        }
    }

    route("/events") {
        post {
            try {
                val req = call.receive<CreateEventRequest>()
                val id = repo.save(
                    ActivityEvent(
                        type = req.type,
                        projectId = req.projectId,
                        taskId = req.taskId,
                        userId = req.userId,
                        payload = req.payload
                    )
                )
                call.respond(HttpStatusCode.Created, CreateEventResponse(id))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }

        get {
            try {
                val projectId = call.request.queryParameters["projectId"]
                val taskId = call.request.queryParameters["taskId"]

                val result = when {
                    projectId != null && taskId != null -> repo.byProjectAndTask(
                        projectId,
                        taskId
                    )

                    projectId != null -> repo.byProject(projectId)
                    taskId != null -> repo.byTask(taskId)
                    else -> {
                        call.respond(HttpStatusCode.BadRequest, "Provide projectId or taskId")
                        return@get
                    }
                }
                call.respond(result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }

        get("/{id}") {
            try {
                val id = call.parameters["id"]!!
                val event = repo.get(id)
                if (event == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(event)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"]!!
                repo.delete(id)
                call.respond(HttpStatusCode.NoContent)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }
    }
}
