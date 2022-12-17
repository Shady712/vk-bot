package com.timetable.service

import com.timetable.client.VkClient
import com.timetable.vk.dto.IncomingMessageDto

class MessageResponseService {
    private val vkClient = VkClient()

    suspend fun handleIncomingMessage(incomingMessageDto: IncomingMessageDto) {
        vkClient.sendMessage(incomingMessageDto.fromId)
    }
}