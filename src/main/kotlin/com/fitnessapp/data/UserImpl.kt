package com.fitnessapp.data

import com.fitnessapp.data.models.User
import com.fitnessapp.data.models.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.fitnessapp.database.DatabaseFactory.dbQuery
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException

class UserImpl : UserDao {
    //реализация интерфейса доступа к таблице users
    private fun resultRowToUser(row: ResultRow) : User {
        //функция преобразования строки ответа в объект User
        return User(
            id = row[UsersTable.id].value,
            userLogin = row[UsersTable.login],
            username = row[UsersTable.username],
            password = row[UsersTable.password],
            salt = row[UsersTable.salt]
        )
    }

    override suspend fun getUserByLogin(login: String): ServiceResult<User> {
        //функция получения пользователя по логину
        val result = dbQuery {
            UsersTable.select(UsersTable.login eq login).singleOrNull()
        } //запрос на получение записи из таблицы с выборкой по логину

        return if (result == null) {
            ServiceResult.Error(ErrorCode.NOT_FOUND) //при отсутствии записи в таблице
        }
        else ServiceResult.Success(resultRowToUser(result))
    }

    override suspend fun getUserById(id: Int): ServiceResult<User> {
        //функция получения пользователя по id
        val result = dbQuery {
            UsersTable.select(UsersTable.id eq id).singleOrNull()
        } //запрос на получение записи из таблицы с выборкой по id

        return if (result == null) {
            ServiceResult.Error(ErrorCode.NOT_FOUND)
        }
        else ServiceResult.Success(resultRowToUser(result))
    }

    override suspend fun insertUser(user: User): ServiceResult<Boolean> {
        //функция добавления пользователя
        return try {
            val result = dbQuery {
                UsersTable.insert {
                    it[login] = user.userLogin
                    it[username] = user.username
                    it[password] = user.password
                    it[salt] = user.salt
                }
            }
            ServiceResult.Success(result.insertedCount > 0)
        } catch (e: SQLException) {
            ServiceResult.Error(ErrorCode.EMAIL_ALREADY_EXISTS)
        } catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun deleteUser(id: Int): ServiceResult<Boolean> {
        //функция удаления пользователя по id
        return try {
            if (entryExists(id)) {
                dbQuery {
                    UsersTable.deleteWhere { UsersTable.id eq id  }
                } //запрос на удаление по id
                ServiceResult.Success(true)
            } else {
                ServiceResult.Error(ErrorCode.NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }

    private suspend fun entryExists(id: Int): Boolean {
        //проверка на существующую запись
        return try {
            val result = dbQuery { UsersTable.select { UsersTable.id eq id }.count() > 0 }
            result
        }
        catch (e: Exception) {
            false
        }
    }
}