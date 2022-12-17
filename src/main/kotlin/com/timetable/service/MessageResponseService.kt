package com.timetable.service

import com.timetable.client.VkClient
import com.timetable.dao.UserDao
import com.timetable.model.User
import com.timetable.vk.dto.IncomingMessageDto
import mu.KLogging

class MessageResponseService(
    private val vkClient: VkClient,
    private val userDao: UserDao
) {

    suspend fun handleIncomingMessage(incomingMessageDto: IncomingMessageDto) {
        logger.debug { "Received incoming message dto: '$incomingMessageDto'" }
        val vkId = incomingMessageDto.fromId.toInt()
        if (userDao.getByVkID(vkId) == null) {
            userDao.insertUser(User(vkId = incomingMessageDto.fromId.toInt()))
            vkClient.sendMessage(vkId, "Привет! Подписывайся на мой гитхаб Shady712!")
        } else {
            vkClient.sendMessage(vkId, "Рад снова тебя видеть!")
        }
    }

    companion object : KLogging()
}