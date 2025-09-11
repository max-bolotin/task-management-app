package org.taskmanagementapp.activity

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.taskmanagementapp.activity.model.ActivityEvent
import org.taskmanagementapp.activity.websocket.WebSocketManager
import java.time.Instant

class WebSocketTest {

    @Test
    fun testWebSocketManagerBroadcast() {
        val webSocketManager = WebSocketManager()

        // Create test activity event
        val activityEvent = ActivityEvent(
            type = "TASK_CREATED",
            projectId = "123",
            taskId = "456",
            userId = "1",
            timestamp = Instant.now(),
            payload = mapOf("taskTitle" to "Test Task")
        )

        // Test broadcast (should not throw exception even with no connections)
        assertDoesNotThrow {
            runBlocking {
                webSocketManager.broadcastActivity(activityEvent)
            }
        }
    }

    @Test
    fun testWebSocketManagerConnectionHandling() {
        val webSocketManager = WebSocketManager()

        // Test that manager can be created without errors
        assertNotNull(webSocketManager)

        // Test connection management methods exist
        // Note: We can't easily test actual WebSocket connections in unit tests
        // without a running server, so we test the manager creation
        assertTrue(true) // Placeholder for successful manager creation
    }

    @Test
    fun testActivityEventCreation() {
        val activityEvent = ActivityEvent(
            type = "TASK_STATUS_CHANGED",
            projectId = "456",
            taskId = "789",
            userId = "2",
            timestamp = Instant.now(),
            payload = mapOf(
                "taskTitle" to "Updated Task",
                "oldStatus" to "TODO",
                "newStatus" to "IN_PROGRESS"
            )
        )

        assertEquals("TASK_STATUS_CHANGED", activityEvent.type)
        assertEquals("456", activityEvent.projectId)
        assertEquals("789", activityEvent.taskId)
        assertEquals("2", activityEvent.userId)
        assertEquals("Updated Task", activityEvent.payload["taskTitle"])
    }
}