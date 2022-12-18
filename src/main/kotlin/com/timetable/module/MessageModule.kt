package com.timetable.module

import com.timetable.dao.ActivityDao
import com.timetable.dao.UserDao
import com.timetable.service.ActivityService
import com.timetable.service.MessageResponseService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messageModule: Module
    get() = module {
        singleOf(::MessageResponseService)
        singleOf(::ActivityService)
        singleOf(::UserDao)
        singleOf(::ActivityDao)
    }