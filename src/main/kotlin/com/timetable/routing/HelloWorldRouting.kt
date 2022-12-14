package com.timetable.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureHelloWorldRouting() {

    routing {
        get("/hello") {
            call.respondText("Hello, World! Shady712")
        }
    }
}
