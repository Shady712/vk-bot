package com.timetable.module

import com.timetable.client.VkClientImpl
import com.timetable.client.VkClientStub
import com.timetable.client.getHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val vkModule: Module
    get() = module {
        single {
            if (System.getenv("ENV") == "prod") VkClientImpl(getHttpClient()) else VkClientStub()
        }
    }