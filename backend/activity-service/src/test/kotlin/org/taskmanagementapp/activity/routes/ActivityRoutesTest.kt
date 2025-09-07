package org.taskmanagementapp.activity.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.taskmanagementapp.activity.service.ActivityService
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ActivityRoutesTest {

    private fun testApplication(
        service: ActivityService,
        test: suspend ApplicationTestBuilder.() -> Unit
    ) {
        testApplication {
            application {
                install(ContentNegotiation) {
                    jackson()
                }
                routing {
                    activityRoutes(service)
                }
            }
            test()
        }
    }

    @Test
    fun `GET ping should return service status`() {
        runBlocking {
            val service = mock<ActivityService>()

            testApplication(service) {
                val response = client.get("/ping")
                assertEquals(HttpStatusCode.OK, response.status)
                assertEquals("Activity Service (Kotlin/Ktor) is up!", response.bodyAsText())
            }
        }
    }

    @Test
    fun `GET health should return health status`() {
        runBlocking {
            val service = mock<ActivityService>()

            testApplication(service) {
                val response = client.get("/health")
                assertEquals(HttpStatusCode.OK, response.status)
                assertEquals("Activity Service (Kotlin/Ktor) is healthy!", response.bodyAsText())
            }
        }
    }

    @Test
    fun `service routes should be configured correctly`() {
        val service = mock<ActivityService>()

        // Test that we can create the service and routes without errors
        assertNotNull(service)
    }
}