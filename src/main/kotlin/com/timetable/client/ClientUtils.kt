package com.timetable.client

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*

fun getHttpClient(): HttpClient = HttpClient {
    install(Logging)
    install(HttpTimeout) {
        connectTimeoutMillis = 20000
        requestTimeoutMillis = 20000
        socketTimeoutMillis = 20000
    }
}
