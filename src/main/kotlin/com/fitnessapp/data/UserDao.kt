package com.fitnessapp.data

import com.fitnessapp.data.models.User
import com.fitnessapp.utils.ServiceResult

interface UserDao {
    //объект доступа к работе с таблицей users
    suspend fun getUserById(id: Int) : ServiceResult<User>
    suspend fun getUserByLogin(login: String) : ServiceResult<User>
    suspend fun insertUser(user : User) : ServiceResult<Boolean>
    suspend fun deleteUser(id: Int) : ServiceResult<Boolean>
}