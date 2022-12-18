package com.timetable.dao

import com.timetable.model.Activity
import com.timetable.model.activity
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

    companion object {
        private val entity = Meta.activity
    }
}