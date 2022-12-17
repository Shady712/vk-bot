package com.timetable.module

import com.timetable.utils.fromResources
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.komapper.dialect.postgresql.r2dbc.PostgreSqlR2dbcDialect
import org.komapper.r2dbc.DefaultR2dbcDatabaseConfig
import org.komapper.r2dbc.R2dbcDatabase
import reactor.core.publisher.toMono

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

        single(named("connectionPool")) {
            ConnectionFactories.get(
                ConnectionFactoryOptions.parse("r2dbc:${get<String>(named("dbUrl"))}")
                    .mutate()
                    .option(ConnectionFactoryOptions.USER, get<String>(named("dbUser")))
                    .option(ConnectionFactoryOptions.PASSWORD, get<String>(named("dbPassword")))
                    .build()
            ).run {
                ConnectionPool(ConnectionPoolConfiguration.builder(this).build()).also {
                    if (System.getenv("ENV") == null) {
                        migrate(it.unwrap())
                    }
                }
            }
        }
        single {
            R2dbcDatabase(
                DefaultR2dbcDatabaseConfig(
                    get<ConnectionPool>(named("connectionPool")).unwrap(), PostgreSqlR2dbcDialect()
                )
            )
        }
    }

@Suppress("DEPRECATION")
private fun migrate(connectionFactory: ConnectionFactory) {
    connectionFactory
        .create().toMono().block()!!
        .createStatement("sql/local.migration.sql".fromResources())
        .execute().toMono().block()
}