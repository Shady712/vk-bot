package com.timetable.dao

import com.timetable.model.User
import com.timetable.model.user
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.r2dbc.R2dbcDatabase

class UserDao(
    private val r2dbc: R2dbcDatabase
) {

    suspend fun insertUser(user: User) =
        r2dbc.runQuery {
            QueryDsl
                .insert(entity)
                .onDuplicateKeyIgnore(entity.vkId)
                .single(user)
        }

    suspend fun getByVkID(vkId: Int) =
        r2dbc.runQuery {
            QueryDsl
                .from(entity)
                .where {
                    vkId eq entity.vkId
                }
                .singleOrNull()
        }

    companion object {
        private val entity = Meta.user
    }
}