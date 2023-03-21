package com.fitnessapp.domain.responses

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String
)
