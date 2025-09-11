package org.taskmanagementapp.activity.websocket

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.taskmanagementapp.activity.model.ActivityEvent
import java.util.concurrent.ConcurrentHashMap

class WebSocketManager {
    private val logger = LoggerFactory.getLogger(WebSocketManager::class.java)
    private val connections = ConcurrentHashMap<String, MutableSet<DefaultWebSocketSession>>()
    private val objectMapper = ObjectMapper().apply {
        registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
        registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
    }

    fun addConnection(projectId: String?, session: DefaultWebSocketSession) {
        val key = projectId ?: "global"
        connections.computeIfAbsent(key) { mutableSetOf() }.add(session)
        logger.info("WebSocket connection added for project: $key. Total connections: ${getTotalConnections()}")
    }

    fun removeConnection(projectId: String?, session: DefaultWebSocketSession) {
        val key = projectId ?: "global"
        connections[key]?.remove(session)
        if (connections[key]?.isEmpty() == true) {
            connections.remove(key)
        }
        logger.info("WebSocket connection removed for project: $key. Total connections: ${getTotalConnections()}")
    }

    suspend fun broadcastActivity(activity: ActivityEvent) {
        val message = objectMapper.writeValueAsString(activity)

        // Broadcast to project-specific connections
        val projectConnections = connections[activity.projectId] ?: emptySet()

        // Broadcast to global connections
        val globalConnections = connections["global"] ?: emptySet()

        val allConnections = projectConnections + globalConnections

        allConnections.forEach { session ->
            try {
                session.send(Frame.Text(message))
            } catch (e: ClosedReceiveChannelException) {
                logger.debug("WebSocket connection closed, removing from manager")
                removeConnection(activity.projectId, session)
                removeConnection(null, session)
            } catch (e: Exception) {
                logger.error("Error sending WebSocket message", e)
            }
        }

        logger.debug("Broadcasted activity to ${allConnections.size} WebSocket connections")
    }

    private fun getTotalConnections(): Int {
        return connections.values.sumOf { it.size }
    }
}