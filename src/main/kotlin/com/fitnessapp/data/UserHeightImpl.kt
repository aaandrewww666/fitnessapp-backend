package com.fitnessapp.data

import com.fitnessapp.data.models.UserHeight
import com.fitnessapp.data.models.UsersHeightTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import com.fitnessapp.database.DatabaseFactory.dbQuery
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult

class UserHeightImpl : UserHeightDao {
    private fun resultRowToUserHeight(row: ResultRow) : UserHeight {
        return UserHeight(
            id = row[UsersHeightTable.id].value,
            userId = row[UsersHeightTable.userId],
            height = row[UsersHeightTable.userHeight]
        )
    }

    override suspend fun getUserHeight(id: Int): ServiceResult<UserHeight> {
        val result = dbQuery {
            UsersHeightTable.select(UsersHeightTable.userId eq id).singleOrNull()
        }

        return if (result == null) {
            ServiceResult.Error(ErrorCode.NOT_FOUND)
        }
        else ServiceResult.Success(resultRowToUserHeight(result))
    }


    override suspend fun insertUserHeight(id : Int, userHeight: Double): ServiceResult<UserHeight> {
        return try {
            if (!idExists(id)) {
                dbQuery {
                    UsersHeightTable.insert {
                        it[userId] = id
                        it[this.userHeight] = userHeight
                    }.resultedValues?.singleOrNull()?.let{
                        ServiceResult.Success(resultRowToUserHeight(it))
                    } ?: ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
            } else {
                ServiceResult.Error(ErrorCode.HEIGHT_ALREADY_FILLED)
            }
        } catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun deleteUserHeight(id: Int): ServiceResult<Boolean> {
        return try {
            if (idExists(id)) {
                dbQuery {
                    UsersHeightTable.deleteWhere { userId eq id }
                }
                ServiceResult.Success(true)
            } else {
                ServiceResult.Success(true)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    private suspend fun idExists(id: Int): Boolean {
        return try {
            val result = dbQuery { UsersHeightTable.select { UsersHeightTable.userId eq id }.count() > 0 }
            result
        }
        catch (e: Exception) {
            false
        }
    }
}