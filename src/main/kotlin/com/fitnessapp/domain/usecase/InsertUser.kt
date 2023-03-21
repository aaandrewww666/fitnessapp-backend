package com.fitnessapp.domain.usecase

import com.fitnessapp.data.models.User
import com.fitnessapp.data.UserDao
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.domain.responses.UserResponse
import com.fitnessapp.utils.ServiceResult

class InsertUser(private val userDao: UserDao){
    suspend operator fun invoke(userInf : User): UserResponse {
        return when (val validation = ValidateUser().invoke(userInf)) {
            is ServiceResult.Success -> {
                when (val user = userDao.insertUser(userInf)) {
                    is ServiceResult.Success -> UserResponse(user.data, emptyList())
                    is ServiceResult.Error -> UserResponse(null, listOf(ResponseErrors(user.error, user.error.message)))
                }
            }
            is ServiceResult.Error -> UserResponse(null, listOf(ResponseErrors(validation.error, validation.error.message)))
        }
    }
}