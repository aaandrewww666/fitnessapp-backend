package com.fitnessapp.domain.requests

@kotlinx.serialization.Serializable
data class SingUpRequest(
    val username: String,
    val userLogin: String,
    val password: String
)
