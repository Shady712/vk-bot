package com.timetable

import com.timetable.routing.configureHelloWorldRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    val env = applicationEngineEnvironment {
        module {
            connector {
                host = "95.163.215.163"
                port = 8080
            }

            install(CallLogging)
            install(ContentNegotiation) {
                json()
            }

            configureHelloWorldRouting()
        }
    }

    embeddedServer(CIO, env).apply {
        start(true)
    }
}
