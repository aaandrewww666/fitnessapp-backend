package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserImpl
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.domain.responses.UserResponse
import com.fitnessapp.utils.ServiceResult

class GetUserById (private val userDao: UserImpl) {
    suspend operator fun invoke(id: Int): UserResponse {
        return when (val user = userDao.getUserById(id)) {
            is ServiceResult.Success -> UserResponse(user.data)
            is ServiceResult.Error -> UserResponse(null)
        }
    }
}
