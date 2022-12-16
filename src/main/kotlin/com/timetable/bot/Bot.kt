package com.timetable.bot

import com.petersamokhin.vksdk.core.client.VkApiClient
import com.petersamokhin.vksdk.core.http.paramsOf
import com.petersamokhin.vksdk.core.model.VkSettings
import com.petersamokhin.vksdk.http.VkKtorHttpClient
import io.ktor.client.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class Bot(groupId: Int, accessToken: String) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

    private val vkHttpClient = VkKtorHttpClient(coroutineContext, HttpClient {
        Logging {
            level = LogLevel.ALL
        }
    })

    private val client = VkApiClient(groupId, accessToken, VkApiClient.Type.Community, VkSettings(vkHttpClient, defaultParams = paramsOf("lang" to "en")))

    fun start() {
        client.onMessage { messageEvent ->
            runBlocking {
                client.sendMessage {
                    peerId = messageEvent.message.peerId
                    message = "Hello, World! Shady712"
                }.execute()
            }
        }
        runBlocking { client.startLongPolling() }
    }
}
