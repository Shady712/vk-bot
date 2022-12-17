import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val muVersion: String by project
val logbackVersion: String by project
val postgresqlVersion: String by project
val vkSdkVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("io.ktor.plugin") version "2.2.1"
}

group = "com.timetable"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:$muVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation(kotlin("test"))
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
