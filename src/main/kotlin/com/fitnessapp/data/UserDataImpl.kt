package com.fitnessapp.data

import com.fitnessapp.data.models.UserData
import com.fitnessapp.data.models.UsersDataTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.fitnessapp.database.DatabaseFactory.dbQuery
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult
import org.jetbrains.exposed.sql.*
import java.sql.SQLException

class UserDataImpl : UserDataDao {
    //реализация интерфейса доступа к таблице users_height
    private fun resultRowToUserHeight(row: ResultRow) : UserData {
        //функция преобразования строки ответа в объект UserHeight
        return UserData(
            id = row[UsersDataTable.id].value,
            userId = row[UsersDataTable.userId],
            height = row[UsersDataTable.height],
            age = row[UsersDataTable.age],
            gender = row[UsersDataTable.gender]
        )
    }

    override suspend fun getUserData(id: Int): ServiceResult<UserData> {
        //функция получения роста пользователя по id
        val result = dbQuery {
            UsersDataTable.select(UsersDataTable.userId eq id).singleOrNull()
        } //получение ответа от запроса к бд с выборкой по id пользователя

        return if (result == null) {
            ServiceResult.Error(ErrorCode.NOT_FOUND) //при пустом ответе возврат ошибки
        }
        else ServiceResult.Success(resultRowToUserHeight(result)) //возврат записи
    }


    override suspend fun insertUserData(id: Int, userHeight: Double, userGender: Int, userAge: Int): ServiceResult<Boolean> {

        return try {
                val result = dbQuery {
                    UsersDataTable.insert {
                        it[userId] = id
                        it[height] = userHeight
                        it[age] = userAge
                        it[gender] = userGender
                    }
                }
                ServiceResult.Success(result.insertedCount > 0)
        } catch (e: SQLException) {
            ServiceResult.Error(ErrorCode.DATA_ALREADY_FILLED)
        } catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun updateUserAge(
        userId: Int,
        userHeight: Double,
        userGender: Int,
        userAge: Int
    ): ServiceResult<Boolean> {
        return try {
            dbQuery {
                UsersDataTable.update({UsersDataTable.userId eq userId} ) {
                    it[height] = userHeight
                    it[age] = userAge
                    it[gender] = userGender
                }
            }
            ServiceResult.Success(true)
        } catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }


    override suspend fun deleteUserData(id: Int): ServiceResult<Boolean> {
        return try {
            if (idExists(id)) {
                dbQuery {
                    UsersDataTable.deleteWhere { userId eq id }
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
        //функция проверки существования записи с id пользователя
        return try {
            val result = dbQuery { UsersDataTable.select {
                UsersDataTable.userId eq id
            }.count() > 0 }
            result
        }
        catch (e: Exception) {
            false
        }
    }
}