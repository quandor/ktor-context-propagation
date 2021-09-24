package com.example

import com.example.plugins.RequestContext
import com.example.plugins.configureMonitoring
import com.example.plugins.configureRouting
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({
            configureRouting(ServiceStatusRepositoryImpl())

            configureMonitoring()
            install(RequestContext)
        }) {
            handleRequest(HttpMethod.Get, "/") { addHeader("X-TrackingId", "me") }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            handleRequest(HttpMethod.Get, "/service-status-api/v1/") { addHeader("X-TrackingId", "trackId") }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
