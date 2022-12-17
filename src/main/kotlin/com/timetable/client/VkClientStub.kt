package com.timetable.client

import mu.KLogging

class VkClientStub : VkClient {

    override suspend fun sendMessage(vkId: Int, text: String) {
        logger.info { "sendMessage for userId '$vkId' with text '$text'" }
    }

    companion object : KLogging()
}