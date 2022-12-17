package com.timetable.client

interface VkClient {
    suspend fun sendMessage(vkId: Int, text: String)
}