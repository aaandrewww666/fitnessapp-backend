package com.fitnessapp.domain.requests

@kotlinx.serialization.Serializable
data class SignInRequest (
    val userLogin: String,
    val password: String
)