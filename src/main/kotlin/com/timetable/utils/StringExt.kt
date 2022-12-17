package com.timetable.utils

import java.nio.charset.StandardCharsets

fun String.fromResources(): String =
    Thread.currentThread().contextClassLoader
        .getResourceAsStream(this)
        .use { it!!.readAllBytes() }
        .toString(StandardCharsets.UTF_8)