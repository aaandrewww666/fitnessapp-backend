package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserWeightDao
import com.fitnessapp.domain.responses.UserWeightServiceResponse
import com.fitnessapp.utils.ServiceResult

class GetUserWeights (private val userWeightDao: UserWeightDao) {
    suspend operator fun invoke(id: Int): UserWeightServiceResponse {
        return when (val userWeights = userWeightDao.getUserWeights(id)) {
            is ServiceResult.Success -> UserWeightServiceResponse(userWeights = userWeights.data)
            is ServiceResult.Error -> UserWeightServiceResponse(error = userWeights.error)
        }
    }
}