package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserWeightDao
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.domain.responses.UserWeightsResponse
import com.fitnessapp.utils.ServiceResult

class GetUserWeights (private val userWeightDao: UserWeightDao) {
    suspend operator fun invoke(id: Int): UserWeightsResponse {
        return when (val userWeight = userWeightDao.getUserWeights(id)) {
            is ServiceResult.Success -> UserWeightsResponse(userWeight.data, emptyList())
            is ServiceResult.Error -> UserWeightsResponse(null,
                listOf(ResponseErrors(userWeight.error, userWeight.error.message)))
        }
    }
}