package com.timetable.service

import com.timetable.client.VkClient
import com.timetable.dao.UserDao
import com.timetable.model.Activity
import com.timetable.model.User
import com.timetable.utils.today
import com.timetable.vk.dto.IncomingMessageDto
import kotlinx.datetime.*
import mu.KLogging
import java.util.concurrent.ConcurrentHashMap

class MessageResponseService(
    private val vkClient: VkClient,
    private val activityService: ActivityService,
    private val userDao: UserDao
) {
    private val activeUsers = ConcurrentHashMap.newKeySet<Int>()

    suspend fun handleIncomingMessage(incomingMessageDto: IncomingMessageDto) {
        logger.debug { "Received incoming message dto: '$incomingMessageDto'" }
        val vkId = incomingMessageDto.fromId.toInt()
        if (!activeUsers.add(vkId) || userDao.getByVkID(vkId) == null) {
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

        if (activeUsers.size > 5) {
            activeUsers.clear()
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

            GET_ACTION -> {
                val period = rawMessage.getOrNull(1)?.lowercase()
                val activities: List<Activity>? = when (period) {
                    null, TODAY -> activityService.getActivitiesForSingleDate(user, today())
                    TOMORROW -> activityService.getActivitiesForSingleDate(user, today().plus(1, DateTimeUnit.DAY))
                    WEEK -> activityService.getActivitiesUntilDate(user, today().plus(1, DateTimeUnit.WEEK))
                    MONTH -> activityService.getActivitiesUntilDate(user, today().plus(1, DateTimeUnit.MONTH))
                    else ->
                        runCatching {
                            activityService.getActivitiesForSingleDate(user, LocalDate.parse(period))
                        }.onFailure {
                            logger.warn { "Getting activities for date '$period' failed with exception '$it'" }
                        }.getOrNull()
                }
                if (activities == null) {
                    vkClient.sendMessage(vkId, MISUNDERSTANDING_RESPONSE)
                } else {
                    vkClient.sendMessage(vkId, activities.toResponse(period ?: TODAY))
                }
            }

            EXAMPLE_ACTION -> vkClient.sendMessage(vkId, EXAMPLE_RESPONSE)
            FAQ_ACTION -> vkClient.sendMessage(vkId, FAQ_RESPONSE)
            else -> vkClient.sendMessage(vkId, MISUNDERSTANDING_RESPONSE)
        }
    }

    private fun List<Activity>.toResponse(period: String): String {
        val sb = StringBuilder().appendLine("Вот твои активности на период: $period").appendLine()
        withIndex().forEach { activity ->
            sb.appendLine("${activity.index + 1}. ${activity.value.name}; длительность: ${activity.value.duration}")
        }
        return sb.toString()
    }

    companion object : KLogging() {
        private const val ADD_ACTION = "добавить"
        private const val EXAMPLE_ACTION = "пример"
        private const val FAQ_ACTION = "faq"
        private const val GET_ACTION = "получить"
        private const val TODAY = "сегодня"
        private const val TOMORROW = "завтра"
        private const val WEEK = "неделя"
        private const val MONTH = "месяц"

        internal const val FAQ_RESPONSE =
            "Чтобы завести новую запись в своем расписании, отправь мне сообщение в формате:\n\n" +
                    "Добавить\n<Название активности>\n<Время в формате yyyy-mm-dd>\n<Продолжительность в формате N h/m>\n\n" +
                    "Чтобы увидеть пример, просто напиши Пример\n\nЧтобы получить записи из своего расписания, напиши сообщение в формате:\n\n" +
                    "Получить\n<Дата в формате yyyy-mm-dd | завтра | неделя | месяц | оставь пустым (тогда получишь дела на сегодня)>"

        internal const val INTRO_RESPONSE =
            "Привет! Я бот VKontekste, помогу тебе следить за твоими делами и распорядком дня.\n$FAQ_RESPONSE\nПодписывайся на мой гитхаб Shady712!"

        internal const val MISUNDERSTANDING_RESPONSE = "Извини, кажется я тебя не понял :(\n$FAQ_RESPONSE"

        internal const val EXAMPLE_RESPONSE = "Добавить\nПомочь кожаному существу понять моё API\n2022-12-31\n5 m\n\n\nПолучить\nНеделя"

        internal const val SUCCESS_ACTIVITY_ADDITION = "Активность успешно добавлена!"
    }
}
