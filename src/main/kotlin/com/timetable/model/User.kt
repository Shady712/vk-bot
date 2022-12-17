package com.timetable.model

import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable

@KomapperEntity
@KomapperTable("users")
data class User(
    @KomapperId
    @KomapperAutoIncrement
    val id: Int = 0,
    val vkId: Int
)
