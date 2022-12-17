package com.timetable

import com.timetable.routing.configureCallbackRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    val env = applicationEngineEnvironment {
        module {
            connector {
                port = 8080
            }
            install(CallLogging)
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                    isLenient = true
                    allowSpecialFloatingPointValues = true
                    allowStructuredMapKeys = true
                    prettyPrint = false
                    useArrayPolymorphism = false
                    ignoreUnknownKeys = true
                })
            }
            install(IgnoreTrailingSlash)

            configureCallbackRouting()
        }
    }

    embeddedServer(CIO, env).apply {
        start(true)
    }
}
