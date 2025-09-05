package org.taskmanagementapp.activity.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.taskmanagementapp.common.dto.HealthStatus

fun Route.healthRoutes() {
    route("/status") {
        get {
            call.respond(HealthStatus.up("activity-service"))
        }
    }
}