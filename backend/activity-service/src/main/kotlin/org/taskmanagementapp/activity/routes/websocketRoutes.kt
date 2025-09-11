package org.taskmanagementapp.activity.routes

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import org.slf4j.LoggerFactory
import org.taskmanagementapp.activity.websocket.WebSocketManager
import java.time.Duration

private val logger = LoggerFactory.getLogger("WebSocketRoutes")

fun Route.websocketRoutes(webSocketManager: WebSocketManager) {
    webSocket("/ws/activities") {
        val projectId = call.request.queryParameters["projectId"]
        logger.info("WebSocket connection established for project: $projectId")

        webSocketManager.addConnection(projectId, this)

        try {
            // Send initial connection confirmation
            send(Frame.Text("""{"type":"connection","status":"connected","projectId":"$projectId"}"""))

            // Keep connection alive and handle incoming messages
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        logger.debug("Received WebSocket message: $text")
                        // Handle client messages if needed (e.g., subscribe to specific projects)
                    }

                    is Frame.Close -> {
                        logger.info("WebSocket connection closed")
                        break
                    }

                    else -> {
                        // Handle other frame types if needed
                    }
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            logger.info("WebSocket connection closed by client")
        } catch (e: Exception) {
            logger.error("WebSocket error", e)
        } finally {
            webSocketManager.removeConnection(projectId, this)
        }
    }
}