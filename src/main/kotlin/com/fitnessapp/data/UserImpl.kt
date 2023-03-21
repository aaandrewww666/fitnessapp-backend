package com.fitnessapp.data

import com.fitnessapp.data.models.User
import com.fitnessapp.data.models.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.fitnessapp.database.DatabaseFactory.dbQuery
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult

class UserImpl : UserDao {
    private fun resultRowToUser(row: ResultRow) : User {
        return User(
            id = row[UsersTable.id].value,
            userLogin = row[UsersTable.login],
            username = row[UsersTable.username],
            password = row[UsersTable.password],
            salt = row[UsersTable.salt]
        )
    }

    override suspend fun getUserByLogin(login: String): ServiceResult<User> {
        val result = dbQuery {
            UsersTable.select(UsersTable.login eq login).singleOrNull()
        }

        return if (result == null) {
            ServiceResult.Error(ErrorCode.NOT_FOUND)
        }
        else ServiceResult.Success(resultRowToUser(result))
        }

    override suspend fun getUserById(id: Int): ServiceResult<User> {
        val result = dbQuery {
            UsersTable.select(UsersTable.id eq id).singleOrNull()
        }

        return if (result == null) {
            ServiceResult.Error(ErrorCode.NOT_FOUND)
        }
        else ServiceResult.Success(resultRowToUser(result))
    }

    override suspend fun insertUser(user: User): ServiceResult<User> {
        return try {
            dbQuery {
                    UsersTable.insert {
                        it[login] = user.userLogin
                        it[username] = user.username
                        it[password] = user.password
                        it[salt] = user.salt
                    }.resultedValues?.singleOrNull()?.let {
                        ServiceResult.Success(resultRowToUser(it))
                    } ?: ServiceResult.Error(ErrorCode.EMAIL_ALREADY_EXISTS)

            }
        } catch (e: Exception) {
            ServiceResult.Error(ErrorCode.DATABASE_ERROR)
        }
    }


    override suspend fun deleteUser(id: Int): ServiceResult<Boolean> {
        return try {
            if (loginExists(id)) {
                dbQuery {
                    UsersTable.deleteWhere { UsersTable.id eq id  }
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

    private suspend fun loginExists(id: Int): Boolean {
        return try {
            val result = dbQuery { UsersTable.select { UsersTable.id eq id }.count() > 0 }
            result
        }
        catch (e: Exception) {
            false
        }
    }
}