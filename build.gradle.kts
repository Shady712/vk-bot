import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val muVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

group = "com.timetable"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$muVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
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
