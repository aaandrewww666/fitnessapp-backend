package com.fitnessapp.domain.requests

@kotlinx.serialization.Serializable
data class SingInRequest (
    val userLogin: String,
    val password: String
)