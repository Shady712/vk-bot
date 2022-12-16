import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val muVersion: String by project
val logbackVersion: String by project
val postgresqlVersion: String by project
val vkSdkVersion: String by project

plugins {
    kotlin("jvm") version "1.7.20"
    application
    id("io.ktor.plugin") version "2.1.3"
}

group = "com.timetable"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
//    implementation("io.ktor:ktor-server-core:$ktorVersion")
//    implementation("io.ktor:ktor-server-cio:$ktorVersion")
//    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
//    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$muVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation ("com.petersamokhin.vksdk:core:$vkSdkVersion")
    implementation ("com.petersamokhin.vksdk:http-client-common-ktor-jvm:$vkSdkVersion")
//    implementation ("com.petersamokhin.vksdk:http-client-jvm-okhttp:$vkSdkVersion")

    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
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
