package org.taskmanagementapp.activity.routes

import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.taskmanagementapp.activity.model.CreateEventRequest
import org.taskmanagementapp.activity.model.CreateEventResponse
import org.taskmanagementapp.activity.service.ActivityService

fun Route.activityRoutes(service: ActivityService) {
    get("/ping", {
        description = "Health check endpoint"
        response {
            HttpStatusCode.OK to {
                description = "Service status"
                body<String> { description = "Status message" }
            }
        }
    }) {
        call.respondText("Activity Service (Kotlin/Ktor) is up!")
    }

    get("/health", {
        description = "Health status endpoint"
        response {
            HttpStatusCode.OK to {
                description = "Health status"
                body<String> { description = "Health message" }
            }
        }
    }) {
        call.respondText("Activity Service (Kotlin/Ktor) is healthy!")
    }

    get("/all", {
        description = "Get all activity events"
        response {
            HttpStatusCode.OK to {
                description = "List of all events"
                body<List<Any>> { description = "Activity events" }
            }
            HttpStatusCode.InternalServerError to {
                description = "Database error"
                body<String> { description = "Error message" }
            }
        }
    }) {
        try {
            val allEntries = service.getAllEvents()
            call.respond(HttpStatusCode.OK, allEntries)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
        }
    }

    get("/activities", {
        description = "Get activities with optional filters"
        request {
            queryParameter<String>("projectId") {
                description = "Filter by project ID"
                required = false
            }
            queryParameter<String>("taskId") {
                description = "Filter by task ID"
                required = false
            }
            queryParameter<Int>("limit") {
                description = "Limit number of results"
                required = false
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "List of activities"
                body<List<Any>> { description = "Activity events" }
            }
        }
    }) {
        try {
            val projectId = call.request.queryParameters["projectId"]
            val taskId = call.request.queryParameters["taskId"]
            val limit = call.request.queryParameters["limit"]?.toIntOrNull()

            val result = if (projectId != null || taskId != null) {
                service.getEvents(projectId, taskId)
            } else {
                service.getAllEvents()
            }

            val limitedResult = if (limit != null && limit > 0) {
                result.take(limit)
            } else {
                result
            }

            call.respond(limitedResult)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
        }
    }

    route("/events") {
        post({
            description = "Create new activity event"
            request {
                body<CreateEventRequest> { description = "Event details" }
            }
            response {
                HttpStatusCode.Created to {
                    description = "Event created successfully"
                    body<CreateEventResponse> { description = "Created event ID" }
                }
                HttpStatusCode.InternalServerError to {
                    description = "Database error"
                    body<String> { description = "Error message" }
                }
            }
        }) {
            try {
                val req = call.receive<CreateEventRequest>()
                val id = service.createEvent(req)
                call.respond(HttpStatusCode.Created, CreateEventResponse(id))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }

        get({
            description = "Get events by project or task"
            request {
                queryParameter<String>("projectId") {
                    description = "Filter by project ID"
                    required = false
                }
                queryParameter<String>("taskId") {
                    description = "Filter by task ID"
                    required = false
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "List of events"
                    body<List<Any>> { description = "Activity events" }
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid parameters"
                    body<String> { description = "Error message" }
                }
                HttpStatusCode.InternalServerError to {
                    description = "Database error"
                    body<String> { description = "Error message" }
                }
            }
        }) {
            try {
                val projectId = call.request.queryParameters["projectId"]
                val taskId = call.request.queryParameters["taskId"]
                val result = service.getEvents(projectId, taskId)
                call.respond(result)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid parameters")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }

        get("/{id}", {
            description = "Get event by ID"
            request {
                pathParameter<String>("id") {
                    description = "Event ID"
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Event details"
                    body<Any> { description = "Activity event" }
                }
                HttpStatusCode.NotFound to {
                    description = "Event not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "Database error"
                    body<String> { description = "Error message" }
                }
            }
        }) {
            try {
                val id = call.parameters["id"]!!
                val event = service.getEventById(id)
                if (event == null) call.respond(HttpStatusCode.NotFound)
                else call.respond(event)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }

        delete("/{id}", {
            description = "Delete event by ID"
            request {
                pathParameter<String>("id") {
                    description = "Event ID"
                    required = true
                }
            }
            response {
                HttpStatusCode.NoContent to {
                    description = "Event deleted successfully"
                }
                HttpStatusCode.NotFound to {
                    description = "Event not found"
                }
                HttpStatusCode.InternalServerError to {
                    description = "Database error"
                    body<String> { description = "Error message" }
                }
            }
        }) {
            try {
                val id = call.parameters["id"]!!
                val deleted = service.deleteEvent(id)
                if (deleted) call.respond(HttpStatusCode.NoContent)
                else call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }
    }

    route("/failed-events") {
        get({
            description = "Get all failed events"
            response {
                HttpStatusCode.OK to {
                    description = "List of failed events"
                    body<List<Any>> { description = "Failed events" }
                }
                HttpStatusCode.InternalServerError to {
                    description = "Database error"
                    body<String> { description = "Error message" }
                }
            }
        }) {
            try {
                val failedEvents = service.getFailedEvents()
                call.respond(failedEvents)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Database error: ${e.message}")
            }
        }
    }
}
