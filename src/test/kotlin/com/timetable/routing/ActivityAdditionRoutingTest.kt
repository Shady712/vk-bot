package com.timetable.routing

import com.timetable.IntegrationTest
import com.timetable.model.User
import com.timetable.util.randomInt
import com.timetable.util.randomString
import com.timetable.vk.dto.IncomingMessageDto
import com.timetable.vk.dto.IncomingObjectDto
import com.timetable.vk.dto.VkCallbackDto
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ActivityAdditionRoutingTest : IntegrationTest() {

    init {
        val vkId = randomInt()
        val userId = runBlocking {
            userDao.insertUser(User(vkId = vkId))
        }.toInt()

        "Add valid activity" {
            routingTest {
                // Arrange
                val name = randomString()
                val date = LocalDate.parse("2022-12-31")
                val duration = "5 h"

                // Act
                val response = client.post("/") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(vkCallbackDto(vkId, name, date.toString(), duration)))
                }

                // Assert
                response.status shouldBe HttpStatusCode.OK
                with(activityDao.getActivitiesByUserIdAndDate(userId, date).first()) {
                    assertSoftly {
                        shouldNotBeNull()
                        this.name shouldBe name
                        this.duration shouldBe duration
                        this.date shouldBe date
                    }
                }
            }
        }

        "Add activity with invalid duration" {
            routingTest {
                // Arrange
                val date = LocalDate.parse("2022-12-15")

                // Act
                val response = client.post("/") {
                    contentType(ContentType.Application.Json)
                    setBody(Json.encodeToString(vkCallbackDto(vkId, randomString(), date.toString(), randomString())))
                }

                // Assert
                response.status shouldBe HttpStatusCode.OK
                activityDao.getActivitiesByUserIdAndDate(userId, date).isEmpty().shouldBeTrue()
            }
        }
    }

    private fun vkCallbackDto(vkId: Int, name: String, date: String, duration: String) =
        VkCallbackDto(
            "message_new",
            IncomingObjectDto(
                IncomingMessageDto(
                    vkId.toString(),
                    "Добавить\n$name\n$date\n$duration"
                )
            )
        )
}