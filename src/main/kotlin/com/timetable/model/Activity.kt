package com.timetable.model

import kotlinx.datetime.LocalDate
import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntity
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperTable

@KomapperEntity
@KomapperTable("activities")
data class Activity(
    @KomapperId
    @KomapperAutoIncrement
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val date: LocalDate,
    val duration: String
)
