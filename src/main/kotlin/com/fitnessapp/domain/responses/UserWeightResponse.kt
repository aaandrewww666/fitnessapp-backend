package com.fitnessapp.domain.responses

import com.fitnessapp.data.models.UserWeight

@kotlinx.serialization.Serializable
data class UserWeightResponse(val userWeight: UserWeight?, val errors: List<ResponseErrors> = emptyList())

@kotlinx.serialization.Serializable
data class UserWeightsResponse(val userWeight: List<UserWeight>?, val errors: List<ResponseErrors> = emptyList())