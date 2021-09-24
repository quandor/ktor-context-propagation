package com.example.plugins

import org.slf4j.event.*
import io.ktor.application.*
import io.ktor.request.*
import java.util.*

fun Application.configureMonitoring() {
    install(EntryExitLogging) {
        level = Level.INFO
        // TODO how to marry RequestContext with this here?
        //  Currently there is a different TrackingId logged than returned
        mdc("trackingId") { it.request.header("X-TrackingId") ?: UUID.randomUUID().toString() }
    }

    install(RequestContext) {val host = this}
}
