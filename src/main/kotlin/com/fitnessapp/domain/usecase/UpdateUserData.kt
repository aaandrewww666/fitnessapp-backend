package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserDataImpl
import com.fitnessapp.domain.requests.DataRequest
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult

class UpdateUserData (private val userDataDao: UserDataImpl){
    suspend operator fun invoke(userId: Int, data: DataRequest): ErrorCode? {
        return when (val result = userDataDao.updateUserAge(
            userId = userId,
            userHeight = data.height,
            userAge = data.age,
            userGender = data.gender)
        ) {
            is ServiceResult.Success -> null
            is ServiceResult.Error -> result.error
        }
    }
}