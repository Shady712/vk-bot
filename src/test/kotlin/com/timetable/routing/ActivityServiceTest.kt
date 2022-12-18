package com.timetable.routing

import com.timetable.IntegrationTest
import com.timetable.model.Activity
import com.timetable.model.User
import com.timetable.service.ActivityService
import com.timetable.util.randomInt
import com.timetable.util.randomString
import com.timetable.utils.today
import io.kotest.data.row
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.koin.test.inject

class ActivityServiceTest : IntegrationTest() {
    private val activityService by inject<ActivityService>()

    init {
        "Getting activities for single date" - {
            listOf(
                row("No activities", 0),
                row("Single activity", 1),
                row("Several activities", 4)
            ).map { (testCase, activitiesCount) ->
                testCase {
                    // Arrange
                    val vkId = randomInt().also { userDao.insertUser(User(vkId = it)) }
                    val user = userDao.getByVkID(vkId)!!
                    val date = today().plus(activitiesCount, DateTimeUnit.DAY)
                    val expectedActivities = mutableListOf<Activity>()
                    repeat(activitiesCount) {
                        val activity = randomActivity(user.id, date)
                        expectedActivities.add(activity)
                        activityDao.insertActivity(activity)
                    }

                    // Act
                    val activities = activityService.getActivitiesForSingleDate(user, date)

                    // Assert
                    activities.shouldForAll { it.date shouldBe date }
                    activities.assertEquals(expectedActivities)
                }
            }
        }

        "Getting activities for date period" {
            // Arrange
            val vkId = randomInt().also { userDao.insertUser(User(vkId = it)) }
            val user = userDao.getByVkID(vkId)!!
            val endDate = today().plus(1, DateTimeUnit.WEEK)
            val expectedActivities = mutableListOf<Activity>()
            repeat(7) { days ->
                repeat(3) {
                    val activity = randomActivity(user.id, today().plus(days, DateTimeUnit.DAY))
                    activityDao.insertActivity(activity)
                    expectedActivities.add(activity)
                }
            }

            // Act
            val activities = activityService.getActivitiesUntilDate(user, endDate)

            // Assert
            activities.assertEquals(expectedActivities)
        }
    }

    private fun List<Activity>.assertEquals(other: List<Activity>) {
        size shouldBe other.size
        forEach { actual ->
            other.firstOrNull { expected ->
                actual.userId == expected.userId && actual.name == expected.name &&
                        actual.date == expected.date && actual.duration == expected.duration
            }.shouldNotBeNull()
        }
    }

    private fun randomActivity(userId: Int, date: LocalDate) = Activity(
        userId = userId,
        name = randomString(),
        date = date,
        duration = "1 h"
    )
}