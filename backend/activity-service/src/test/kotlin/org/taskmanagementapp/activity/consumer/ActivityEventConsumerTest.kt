package org.taskmanagementapp.activity.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.taskmanagementapp.activity.repo.ActivityRepository

class ActivityEventConsumerTest {

    @Test
    fun `consumer lifecycle should work correctly`() {
        runBlocking {
            val repository = mock<ActivityRepository>()
            val objectMapper = ObjectMapper().apply {
                registerModule(KotlinModule.Builder().build())
                registerModule(JavaTimeModule())
            }
            val kafkaConfig = mapOf(
                "bootstrap.servers" to "localhost:9092",
                "group.id" to "test-group",
                "topic" to "test-topic",
                "auto.offset.reset" to "earliest"
            )
            val consumer = ActivityEventConsumer(repository, kafkaConfig, objectMapper)

            consumer.start()
            delay(100) // Let it start
            consumer.stop()
            delay(100) // Let it stop

            // Test passes if no exceptions thrown
        }
    }

    @Test
    fun `consumer should be created successfully`() {
        val repository = mock<ActivityRepository>()
        val objectMapper = ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            registerModule(JavaTimeModule())
        }
        val kafkaConfig = mapOf(
            "bootstrap.servers" to "localhost:9092",
            "group.id" to "test-group",
            "topic" to "test-topic",
            "auto.offset.reset" to "earliest"
        )

        val consumer = ActivityEventConsumer(repository, kafkaConfig, objectMapper)

        // Test that consumer can be created without errors
        kotlin.test.assertNotNull(consumer)
    }
}