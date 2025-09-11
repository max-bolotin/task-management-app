package org.taskmanagementapp.activity.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.*
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.taskmanagementapp.activity.model.ActivityEvent
import org.taskmanagementapp.activity.model.FailedEvent
import org.taskmanagementapp.activity.repo.ActivityRepository
import org.taskmanagementapp.common.dto.ActivityEvent as CommonActivityEvent
import java.time.Duration
import java.util.*

class ActivityEventConsumer(
    private val repository: ActivityRepository,
    private val kafkaConfig: Map<String, String>,
    private val objectMapper: ObjectMapper,
    private val webSocketManager: org.taskmanagementapp.activity.websocket.WebSocketManager
) {
    private val logger = LoggerFactory.getLogger(ActivityEventConsumer::class.java)
    private var consumerJob: Job? = null

    fun start() {
        consumerJob = CoroutineScope(Dispatchers.IO).launch {
            val props = Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig["bootstrap.servers"])
                put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig["group.id"])
                put(
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer::class.java.name
                )
                put(
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer::class.java.name
                )
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfig["auto.offset.reset"])
                put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
            }

            val consumer = KafkaConsumer<String, String>(props)
            consumer.subscribe(listOf(kafkaConfig["topic"] ?: "activity-events"))

            logger.info("Started Kafka consumer for topic: {}", kafkaConfig["topic"])

            try {
                while (isActive) {
                    val records = consumer.poll(Duration.ofMillis(1000))
                    for (record in records) {
                        try {
                            handleEvent(record.value())
                        } catch (e: Exception) {
                            logger.error("Failed to process event: {}", record.value(), e)
                            try {
                                repository.saveFailedEvent(
                                    FailedEvent(
                                        originalEvent = record.value(),
                                        errorMessage = e.message ?: "Unknown error"
                                    )
                                )
                            } catch (dlqError: Exception) {
                                logger.error("Failed to save to dead letter queue", dlqError)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                logger.error("Kafka consumer error", e)
            } finally {
                consumer.close()
                logger.info("Kafka consumer closed")
            }
        }
    }

    fun stop() {
        consumerJob?.cancel()
        logger.info("Kafka consumer stopped")
    }

    private suspend fun handleEvent(eventJson: String) {
        try {
            val commonEvent = objectMapper.readValue(eventJson, CommonActivityEvent::class.java)
            val activityEvent = ActivityEvent(
                type = commonEvent.eventType.name,
                projectId = commonEvent.projectId.toString(),
                taskId = commonEvent.taskId?.toString(),
                userId = commonEvent.userId.toString(),
                timestamp = commonEvent.timestamp.atZone(java.time.ZoneId.systemDefault())
                    .toInstant(),
                payload = commonEvent.metadata ?: emptyMap()
            )

            val savedId = repository.save(activityEvent)
            logger.debug("Saved activity event: {} with ID: {}", commonEvent.eventType, savedId)

            // Broadcast to WebSocket clients
            webSocketManager.broadcastActivity(activityEvent)
        } catch (e: Exception) {
            logger.error("Failed to handle event: {}", eventJson, e)
            throw e
        }
    }
}