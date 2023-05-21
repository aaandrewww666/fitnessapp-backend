package com.fitnessapp.domain.responses

import com.fitnessapp.data.models.UserWeight
import com.fitnessapp.utils.ErrorCode

data class UserWeightServiceResponse(
    val userWeights: List<UserWeight>? = null,
    val error: ErrorCode? = null
)