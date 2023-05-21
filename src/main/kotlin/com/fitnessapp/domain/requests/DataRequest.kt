package com.fitnessapp.domain.requests

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DataRequest(
    @SerialName("height") val height: Double,
    @SerialName("gender") val gender: Int,
    @SerialName("age") val age: Int
)
