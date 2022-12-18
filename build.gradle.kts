import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val muVersion: String by project
val logbackVersion: String by project
val postgresqlVersion: String by project
val vkSdkVersion: String by project
val r2dbcVersion: String by project
val koinVersion: String by project
val komapperVersion: String by project
val kotlinxDatetimeVersion: String by project
val kotestVersion: String by project
val kotestKoinVersion: String by project
val mockkVersion: String by project
val testcontainersVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("io.ktor.plugin") version "2.2.1"
    id("com.google.devtools.ksp") version "1.7.20-1.0.6"
}

group = "com.timetable"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    // koin
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    // db
    implementation("io.r2dbc:r2dbc-pool:$r2dbcVersion")
    implementation("org.postgresql:r2dbc-postgresql:$r2dbcVersion")

    // komapper
    platform("org.komapper:komapper-platform:$komapperVersion").let {
        implementation(it)
        ksp(it)
    }
    ksp("org.komapper:komapper-processor")
    implementation("org.komapper:komapper-starter-r2dbc")
    implementation("org.komapper:komapper-dialect-postgresql-r2dbc")

    // utils
    implementation("io.github.microutils:kotlin-logging-jvm:$muVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")

    // test
    testImplementation(kotlin("test-common"))
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-koin:$kotestKoinVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
}

application {
    mainClass.set("com.timetable.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}