package com.fitnessapp.domain.requests

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class WeightRequest (
    @SerialName("userWeight") val userWeight: Double,
    @SerialName("date") val date: String
)