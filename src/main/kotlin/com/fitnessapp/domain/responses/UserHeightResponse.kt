package com.fitnessapp.domain.responses

import com.fitnessapp.data.models.UserHeight
import com.fitnessapp.domain.responses.ResponseErrors

@kotlinx.serialization.Serializable
data class UserHeightResponse(val userHeight: UserHeight?, val errors: List<ResponseErrors> = emptyList())
