package com.timetable.service

import com.timetable.dao.ActivityDao
import com.timetable.exception.VkBotException
import com.timetable.model.Activity
import com.timetable.model.User
import kotlinx.datetime.LocalDate
import mu.KLogging

class ActivityService(
    private val activityDao: ActivityDao
) {

    suspend fun addActivity(user: User, rawMessage: List<String>): Boolean {
        logger.info { "Adding activity for user '$user'" }
        return runCatching {
            val name = rawMessage[1]
            val date = LocalDate.parse(rawMessage[2])
            val duration = rawMessage[3].also {
                if (!DURATION_REGEX.matches(it)) {
                    throw VkBotException("Invalid duration")
                }
            }
            activityDao.insertActivity(
                Activity(
                    userId = user.id,
                    name = name,
                    date = date,
                    duration = duration
                )
            )
            true
        }.onFailure {
            logger.warn { "Adding activity failed with exception: '$it'" }
        }.getOrDefault(false)
    }

    companion object : KLogging() {
        private val DURATION_REGEX = Regex("\\d+ [hm]")
    }
}