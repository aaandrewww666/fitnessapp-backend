package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserWeightImpl
import com.fitnessapp.domain.requests.WeightRequest
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult

class InsertUserWeight(private val userWeightImpl: UserWeightImpl) {
    suspend operator fun invoke(userId: Int, weightRequest: WeightRequest): ErrorCode? {
        return when (val userWeight = userWeightImpl.insertUserWeight(userId, weightRequest)) {
            is ServiceResult.Success -> null
            is ServiceResult.Error -> userWeight.error
        }
    }
}