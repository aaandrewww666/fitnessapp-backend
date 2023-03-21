package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserWeightImpl
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.domain.responses.UserWeightResponse
import com.fitnessapp.utils.ServiceResult

class InsertUserWeight(private val userWeightImpl: UserWeightImpl) {
    suspend operator fun invoke(userId: Int, weight: Double): UserWeightResponse {
        return when (val userWeight = userWeightImpl.insertUserWeight(userId, weight)) {
            is ServiceResult.Success -> UserWeightResponse(userWeight.data, emptyList())
            is ServiceResult.Error -> UserWeightResponse(
                null,
                listOf(ResponseErrors(userWeight.error, userWeight.error.message))
            )
        }
    }
}