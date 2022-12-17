package com.timetable.routing

import com.timetable.service.MessageResponseService
import com.timetable.vk.dto.VkCallbackDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureCallbackRouting() {
    val messageResponseService = MessageResponseService()

    routing {
        post("/") {
            val vkCallbackDto = call.receive<VkCallbackDto>()
            when (vkCallbackDto.type) {
                "confirmation" -> call.respondText("f0477966", contentType = ContentType.Text.Plain)
                "message_new" -> {
                    call.respondText("ok")
                    messageResponseService.handleIncomingMessage(vkCallbackDto.incomingObjectDto!!.message)
                }
            }
        }
    }
}
