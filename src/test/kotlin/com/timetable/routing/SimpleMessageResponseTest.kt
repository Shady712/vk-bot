package com.timetable.routing

import com.timetable.IntegrationTest
import com.timetable.client.VkClient
import com.timetable.service.ActivityService
import com.timetable.service.MessageResponseService
import com.timetable.service.MessageResponseService.Companion.EXAMPLE_RESPONSE
import com.timetable.service.MessageResponseService.Companion.FAQ_RESPONSE
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
    private val vkId = randomInt()

    init {
        "Simple messages" - {
            listOf(
                row("Introduction", "Привет", INTRO_RESPONSE),
                row("Misunderstanding", randomString(), MISUNDERSTANDING_RESPONSE),
                row("Example", "приМер", EXAMPLE_RESPONSE),
                row("FAQ", "fAQ", FAQ_RESPONSE)
            ).map { (testCase, messageText, responseText) ->
                testCase {
                    simpleMessageResponseTest(messageText, responseText)
                }
            }
        }

        "Activity addition misunderstanding" - {
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
        // Arrange
        val vkClientMock = mockk<VkClient>()
        coJustRun { vkClientMock.sendMessage(any(), any()) }
        val messageResponseService = MessageResponseService(
            vkClientMock,
            activityService,
            userDao
        )

        // Act
        messageResponseService.handleIncomingMessage(
            IncomingMessageDto(
                vkId.toString(),
                messageText
            )
        )

        // Assert
        coVerify(exactly = 1) { vkClientMock.sendMessage(vkId, responseString) }
    }
}
