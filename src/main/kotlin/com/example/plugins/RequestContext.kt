package com.example.plugins

import io.ktor.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.*
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class RequestContext {

    suspend fun trackingId(): String {
        return getRequestContextData().trackingId
    }

    suspend fun requestStartTime(): Instant {
        return getRequestContextData().requestStartTime
    }

    private suspend fun getRequestContextData() = currentCoroutineContext()[Key] ?: error("That should never happen")

    companion object Plugin : ApplicationFeature<Application, Configuration, RequestContext> {
        override val key: AttributeKey<RequestContext> = AttributeKey("RequestContext")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): RequestContext {
            val requestContextPhase = PipelinePhase("RequestContextPhase")
            pipeline.insertPhaseBefore(ApplicationCallPipeline.Monitoring, requestContextPhase)
            pipeline.intercept(requestContextPhase) {
                val trackingId = call.request.headers["X-TrackingId"] ?: UUID.randomUUID().toString()
                withContext(RequestContextData(trackingId, Instant.now())) {
                    proceed()
                }
            }
            return requestContext
        }
    }
}

val requestContext = RequestContext()

class Configuration

private object Key : CoroutineContext.Key<RequestContextData>

private class RequestContextData(val trackingId: String, val requestStartTime: Instant) : AbstractCoroutineContextElement(
    Key
)

