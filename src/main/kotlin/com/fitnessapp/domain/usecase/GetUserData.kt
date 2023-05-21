package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserDataDao
import com.fitnessapp.domain.responses.UserDataServiceResponse
import com.fitnessapp.utils.ServiceResult

class GetUserData (private val userDataDao: UserDataDao) {
    suspend operator fun invoke(id: Int): UserDataServiceResponse {
        return when (val result = userDataDao.getUserData(id)) {
            is ServiceResult.Success -> UserDataServiceResponse(result.data)
            is ServiceResult.Error -> UserDataServiceResponse(error = result.error)
        }
    }
}