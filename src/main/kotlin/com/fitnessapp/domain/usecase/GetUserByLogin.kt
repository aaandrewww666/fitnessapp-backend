package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserImpl
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.domain.responses.UserResponse
import com.fitnessapp.utils.ServiceResult

class GetUserByLogin(private val userDao: UserImpl) {
    suspend operator fun invoke(login: String): UserResponse {
        return when (val user = userDao.getUserByLogin(login)) {
            is ServiceResult.Success -> UserResponse(user.data)
            is ServiceResult.Error -> UserResponse(null)
        }
    }
}