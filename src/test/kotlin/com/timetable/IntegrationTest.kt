package com.timetable

import com.timetable.container.PostgresContainer
import com.timetable.dao.ActivityDao
import com.timetable.dao.UserDao
import com.timetable.module.dbConnectionModule
import com.timetable.module.messageModule
import com.timetable.module.vkModule
import com.timetable.routing.configureCallbackRouting
import io.kotest.core.spec.style.FreeSpec
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

open class IntegrationTest : FreeSpec(), KoinTest {
    protected val activityDao by inject<ActivityDao>()
    protected val userDao by inject<UserDao>()

    companion object {
        private val application: TestApplication

        init {
            startKoin {
                modules(
                    messageModule,
                    vkModule,
                    PostgresContainer.dbConfigModule(),
                    dbConnectionModule
                )
            }
            application = TestApplication {
                application {
                    configureCallbackRouting()
                }
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
                install(CallLogging)
                install(IgnoreTrailingSlash)
                environment {
                    developmentMode = false
                }
            }
        }
    }

    protected suspend fun routingTest(block: suspend TestApplication.() -> Unit) =
        application.block()
}