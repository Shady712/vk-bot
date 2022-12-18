package com.timetable.routing

import com.timetable.IntegrationTest
import com.timetable.client.VkClient
import com.timetable.dao.UserDao
import com.timetable.service.ActivityService
import com.timetable.service.MessageResponseService
import com.timetable.service.MessageResponseService.Companion.INTRO_RESPONSE
import com.timetable.service.MessageResponseService.Companion.MISUNDERSTANDING_RESPONSE
import com.timetable.util.randomInt
import com.timetable.util.randomString
import com.timetable.vk.dto.IncomingMessageDto
import io.kotest.data.row
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import org.koin.test.inject

class SimpleMessageResponseTest : IntegrationTest() {
    private val activityService by inject<ActivityService>()
    private val userDao by inject<UserDao>()
    private val vkId = randomInt()

    init {
        "Introduction test" {
            simpleMessageResponseTest("Привет", INTRO_RESPONSE)
        }

        "Misunderstanding test" {
            simpleMessageResponseTest(randomString(), MISUNDERSTANDING_RESPONSE)
        }

        "Activity addition misunderstanding tests" - {
            listOf(
                row(
                    "Invalid date format",
                    "Добавить\n${randomString()}\n${randomString()}\n1 h"
                ),
                row(
                    "Invalid duration format",
                    "Добавить\n${randomString()}\n2022-12-31\n${randomString()}"
                )
            ).map { (testCase, messageText) ->
                testCase {
                    simpleMessageResponseTest(messageText, MISUNDERSTANDING_RESPONSE)
                }
            }
        }
    }

    private suspend fun simpleMessageResponseTest(messageText: String, responseString: String) {
        val vkClientMock = mockk<VkClient>()
        coJustRun { vkClientMock.sendMessage(any(), any()) }
        val messageResponseService = MessageResponseService(
            vkClientMock,
            activityService,
            userDao
        )
        messageResponseService.handleIncomingMessage(
            IncomingMessageDto(
                vkId.toString(),
                messageText
            )
        )
        coVerify(exactly = 1) { vkClientMock.sendMessage(vkId, responseString) }
    }
}
