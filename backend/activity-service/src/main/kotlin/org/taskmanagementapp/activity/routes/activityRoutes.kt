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

fun Application.activityRoutes(repo: ActivityRepository) {
    routing {
        get("/ping") {
            call.respondText("Activity Service (Kotlin/Ktor) is up!")
        }

        route("/events") {
            post {
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
            }

            get {
                val projectId = call.request.queryParameters["projectId"]
                val taskId = call.request.queryParameters["taskId"]

                val result = when {
                    projectId != null -> repo.byProject(projectId)
                    taskId != null -> repo.byTask(taskId)
                    else -> {
                        call.respond(HttpStatusCode.BadRequest, "Provide projectId or taskId")
                        return@get
                    }
                }
                call.respond(result)
            }

            get("/{id}") {
                val id = call.parameters["id"]!!
                val event = repo.get(id)
                if (event == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(event)
            }
        }
    }
}
