package com.timetable.util

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils
import kotlin.random.Random

fun randomInt() = Random.nextInt()
fun randomString() = RandomStringUtils.randomAlphabetic(32)