package com.timetable.client

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.random.Random

class VkClientImpl(
    private val client: HttpClient
) : VkClient {

    override suspend fun sendMessage(vkId: Int, text: String) {
        client.post("https://api.vk.com/method/messages.send") {
            parameter("group_id", GROUP_ID)
            parameter("access_token", ACCESS_TOKEN)
            parameter("v", "5.131")
            parameter("user_id", vkId)
            parameter("message", text)
            parameter("random_id", Random.nextInt())
        }
    }

    companion object {
        private const val GROUP_ID = "217746000"
        private const val ACCESS_TOKEN = "vk1.a.LM2U58gxDgJX8I55_PIg6ZfmrSewYdZ1ghYzlh9ulm4mSEqZld5RHHYtTFbOvja2pQQRsDBbzb3DIitTBpxffoDo8oC5YV-nZMTBv1XbHnQJI-XbuhNif7dTrlT_rVCcBJgVPgIkk31HNYhRVS-O4CW_VwP9VbE191uj5xji8Jzv12TUliqUcHxafCcl6KkqtR2RbJgGHzxNLQIZtDc44w"
    }
}