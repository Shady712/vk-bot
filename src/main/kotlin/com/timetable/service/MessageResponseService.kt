package com.timetable.service

import com.timetable.client.VkClient
import com.timetable.dao.UserDao
import com.timetable.model.User
import com.timetable.vk.dto.IncomingMessageDto
import mu.KLogging

class MessageResponseService(
    private val vkClient: VkClient,
    private val activityService: ActivityService,
    private val userDao: UserDao
) {

    suspend fun handleIncomingMessage(incomingMessageDto: IncomingMessageDto) {
        logger.debug { "Received incoming message dto: '$incomingMessageDto'" }
        val vkId = incomingMessageDto.fromId.toInt()
        if (userDao.getByVkID(vkId) == null) {
            userDao.insertUser(User(vkId = incomingMessageDto.fromId.toInt()))
            vkClient.sendMessage(vkId, INTRO_RESPONSE)
        } else {
            val lines = incomingMessageDto.text.lines().map { it.trim() }
            if (lines.isEmpty()) {
                vkClient.sendMessage(vkId, MISUNDERSTANDING_RESPONSE)
            } else {
                handleRequestMessage(vkId, lines)
            }
        }
    }

    private suspend fun handleRequestMessage(vkId: Int, rawMessage: List<String>) {
        vkClient.sendMessage(vkId, "Рад снова тебя видеть!")
        val user = userDao.getByVkID(vkId)!!
        when (rawMessage.first().lowercase()) {
            ADD_ACTION -> {
                if (activityService.addActivity(user, rawMessage)) {
                    vkClient.sendMessage(vkId, SUCCESS_ACTIVITY_ADDITION)
                } else {
                    vkClient.sendMessage(vkId, MISUNDERSTANDING_RESPONSE)
                }
            }
            EXAMPLE_ACTION -> vkClient.sendMessage(vkId, EXAMPLE_RESPONSE)
            FAQ_ACTION -> vkClient.sendMessage(vkId, FAQ_RESPONSE)
            else -> vkClient.sendMessage(vkId, MISUNDERSTANDING_RESPONSE)
        }
    }

    companion object : KLogging() {
        private const val ADD_ACTION = "добавить"
        private const val EXAMPLE_ACTION = "пример"
        private const val FAQ_ACTION = "faq"

        internal const val FAQ_RESPONSE =
            "Чтобы завести новую запись в своем расписании, отправь мне сообщение в формате:\n\n" +
                    "Добавить\n'<Название активности>\n<Время в формате yyyy-mm-dd>\n<Продолжительность в формате N h/m>\n\n" +
                    "Чтобы увидеть пример, просто напиши Пример"

        internal const val INTRO_RESPONSE =
            "Привет! Я бот VKontekste, помогу тебе следить за твоими делами и распорядком дня.\n$FAQ_RESPONSE\nПодписывайся на мой гитхаб Shady712!"

        internal const val MISUNDERSTANDING_RESPONSE = "Извини, кажется я тебя не понял :(\n$FAQ_RESPONSE"

        internal const val EXAMPLE_RESPONSE = "Добавить\nПомочь кожаному существу понять моё API\n2022-12-31\n5 m}"

        internal const val SUCCESS_ACTIVITY_ADDITION = "Активность успешно добавлена!"
    }
}
