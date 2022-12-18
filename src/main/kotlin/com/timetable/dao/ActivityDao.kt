package com.timetable.dao

import com.timetable.model.Activity
import com.timetable.model.activity
import kotlinx.datetime.LocalDate
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.r2dbc.R2dbcDatabase

class ActivityDao(
    private val r2dbc: R2dbcDatabase
) {

    suspend fun insertActivity(activity: Activity) =
        r2dbc.runQuery {
            QueryDsl
                .insert(entity)
                .single(activity)
        }

    suspend fun getActivitiesByUserIdAndDate(userId: Int, date: LocalDate) =
        r2dbc.runQuery {
            QueryDsl
                .from(entity)
                .where {
                    entity.userId eq userId
                    entity.date eq date
                }
        }

    suspend fun getActivitiesByUserIdAndStartDateAndEndDate(userId: Int, startDate: LocalDate, endDate: LocalDate) =
        r2dbc.runQuery {
            QueryDsl
                .from(entity)
                .where {
                    entity.userId eq userId
                    startDate lessEq entity.date
                    entity.date lessEq endDate
                }
        }

    companion object {
        private val entity = Meta.activity
    }
}