package com.fitnessapp.data

import com.fitnessapp.data.models.User
import com.fitnessapp.utils.ServiceResult

interface UserDao {
    suspend fun getUserById(id: Int) : ServiceResult<User>
    suspend fun getUserByLogin(login: String) : ServiceResult<User>
    suspend fun insertUser(user : User) : ServiceResult<User>
    suspend fun deleteUser(id: Int) : ServiceResult<Boolean>
}