package com.timetable.module

import com.timetable.client.VkClientImpl
import com.timetable.client.VkClientStub
import com.timetable.client.getHttpClient
import com.timetable.dao.UserDao
import com.timetable.service.MessageResponseService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messageModule: Module
    get() = module {
        singleOf(::MessageResponseService)
        singleOf(::UserDao)
        single {
            if (System.getenv("ENV") == "prod") VkClientImpl(getHttpClient()) else VkClientStub()
        }
    }