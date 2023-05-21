package com.fitnessapp.domain.responses

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DataResponse(
    @SerialName("height") val height: Double,
    @SerialName("gender") val gender: Int,
    @SerialName("age") val age: Int
)
