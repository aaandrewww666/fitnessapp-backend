package com.fitnessapp.domain.responses

import com.fitnessapp.data.models.UserData
import com.fitnessapp.utils.ErrorCode

data class UserDataServiceResponse(
    val userData: UserData? = null,
    val error: ErrorCode? = null
    )

