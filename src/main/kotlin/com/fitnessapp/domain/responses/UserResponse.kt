package com.fitnessapp.domain.responses

import com.fitnessapp.data.models.User
import com.fitnessapp.domain.responses.ResponseErrors

@kotlinx.serialization.Serializable
data class UserResponse(val user: User?, val errors: List<ResponseErrors> = emptyList())

@kotlinx.serialization.Serializable
data class DeleteResponse(val success: Boolean = false, val errors: List<ResponseErrors> = emptyList())

