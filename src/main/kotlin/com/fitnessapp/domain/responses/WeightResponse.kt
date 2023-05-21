package com.fitnessapp.domain.responses

import com.fitnessapp.domain.requests.WeightRequest
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class WeightResponse(
    @SerialName("userWeights") val userWeights: List<WeightRequest>
)