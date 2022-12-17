package com.timetable.vk.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VkCallbackDto(
    val type: String,
    @SerialName("object")
    val incomingObjectDto: IncomingObjectDto? = null
)

@Serializable
data class IncomingObjectDto(
    val message: IncomingMessageDto? = null
)

@Serializable
data class IncomingMessageDto(
    @SerialName("from_id")
    val fromId: String = "stub",
    val text: String = "stub"
)
