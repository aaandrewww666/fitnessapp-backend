package com.fitnessapp.domain.requests

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SignUpRequest(
    @SerialName("password") val password: String,
    @SerialName("userLogin") val userLogin: String,
    @SerialName("username") val username: String
)
