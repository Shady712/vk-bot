package com.timetable.module

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dbConfigModule: Module
    get() = module(createdAtStart = true) {
        single(named("dbUrl")) {
            System.getenv("DB_URL") ?: "postgresql://localhost:5432/vk-bot"
        }
        single(named("dbUser")) {
            System.getenv("DB_USER") ?: "postgres"
        }
        single(named("dbPassword")) {
            System.getenv("DB_PASSWORD") ?: "password"
        }
    }
