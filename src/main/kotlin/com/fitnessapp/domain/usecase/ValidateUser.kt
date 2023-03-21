package com.fitnessapp.domain.usecase

import com.fitnessapp.data.models.User
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult

class ValidateUser {

    operator fun invoke(user: User): ServiceResult<Boolean> {
        return if (user.userLogin.isBlank() && user.password.isBlank()) {
            ServiceResult.Error(ErrorCode.BADLY_FORMATTED_INPUT)
        }
        else {
            if (ValidateLogin().invoke(user.userLogin)) ServiceResult.Success(true)
            else ServiceResult.Error(ErrorCode.INVALID_EMAIL)
        }
    }
}