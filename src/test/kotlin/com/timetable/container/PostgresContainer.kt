package com.timetable.container

import mu.KLogging
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.utility.DockerImageName
import java.time.Duration

interface PostgresContainer {

    companion object : KLogging() {
        private val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:12.5")).apply {
            waitingFor(HostPortWaitStrategy())
            withStartupTimeout(Duration.ofMinutes(3))
            withTmpFs(mapOf("/var/lib/postgresql/data" to "rw"))
            start()
            logger.info { "Started postgres at port ${getMappedPort(5432)}" }
        }

        fun dbConfigModule() = module {
            single(named("dbUrl")) {
                "postgresql://${postgres.host}:${postgres.getMappedPort(5432)}/${postgres.databaseName}"
            }
            single(named("dbUser")) {
                postgres.username
            }
            single(named("dbPassword")) {
                postgres.password
            }
        }
    }
}