package com.fitnessapp.domain.responses

import com.fitnessapp.utils.ErrorCode

@kotlinx.serialization.Serializable
data class ResponseErrors(val error: ErrorCode, val message: String)