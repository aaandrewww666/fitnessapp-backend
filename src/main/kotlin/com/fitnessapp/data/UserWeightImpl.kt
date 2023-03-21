package com.fitnessapp.data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.fitnessapp.data.models.UserWeight
import com.fitnessapp.data.models.UsersWeightTable
import com.fitnessapp.database.DatabaseFactory.dbQuery
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult
import com.fitnessapp.utils.toDatabaseString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserWeightImpl : UserWeightDao {
    private fun resultRowToUserWeight(row: ResultRow) : UserWeight {
        return UserWeight(
            userId = row[UsersWeightTable.userId],
            weight = row[UsersWeightTable.weight],
            date = row[UsersWeightTable.date].toDatabaseString()
        )
    }

    override suspend fun getUserWeights(id: Int): ServiceResult<List<UserWeight>> {
        val result =
            dbQuery {
            UsersWeightTable.select(UsersWeightTable.userId eq id).map (::resultRowToUserWeight)
        }
        return if (result.isEmpty()) {
            ServiceResult.Error(ErrorCode.NOT_FOUND)
        } else ServiceResult.Success(result)

    }

    override suspend fun insertUserWeight(userId : Int, userWeight: Double): ServiceResult<UserWeight> {
        return try {
            if (checkWeightByDate(userId, LocalDate.now())) {
                dbQuery {
                    UsersWeightTable.insert {
                        it[this.userId] = userId
                        it[weight] = userWeight
                    }.resultedValues?.singleOrNull()?.let {
                        ServiceResult.Success(resultRowToUserWeight(it))
                    } ?: ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
            } else {
                ServiceResult.Error(ErrorCode.WEIGHT_ALREADY_FILLED)
            }
        } catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun deleteUserWeights(id: Int): ServiceResult<Boolean> {
        return try {
            if (idExists(id)) {
                dbQuery {
                    UsersWeightTable.deleteWhere { userId eq id }
                }
                ServiceResult.Success(true)
            } else {
                ServiceResult.Error(ErrorCode.NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun deleteUserWeightByDate(id: Int, date: String): ServiceResult<Boolean> {
        return try {
                dbQuery {
                    UsersWeightTable.deleteWhere {
                        userId eq id and(UsersWeightTable.date eq LocalDate.parse(date, DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd"
                        )))
                    }
                }
                ServiceResult.Success(true)
        }
        catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    private suspend fun idExists(id: Int): Boolean {
        /*проверка существования записей с userId в таблице users_weight*/
        return try {
            val result = dbQuery {
                UsersWeightTable.select {
                    UsersWeightTable.userId eq id
                }.count() > 0
            }
            result
        }
        catch (e: Exception) {
            false
        }
    }

    private suspend fun checkWeightByDate(id : Int, date : LocalDate) : Boolean {
        /*проверка на существование записи о весе пользователя в текущий день*/
        return try {
            val result = dbQuery {
                UsersWeightTable.select {
                    UsersWeightTable.date eq date and (UsersWeightTable.userId eq id)
                }.count() < 1} //запрос к бд к таблице users_weight на получение записей,
                               // соответствующих условию, далее проверка на количество этих записей
            result
        } catch (e : Exception) {
            true
        }
    }
}