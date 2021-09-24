package com.example

import com.example.plugins.configureMonitoring
import com.example.plugins.requestContext
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureMonitoring()
        routing {
            get("/") {
                call.respond("Called with TrackingId ${requestContext.trackingId()}")
            }
        }
    }.start(wait = true)
}
