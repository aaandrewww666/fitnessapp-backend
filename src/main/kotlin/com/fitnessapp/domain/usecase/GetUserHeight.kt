package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserHeightDao
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.domain.responses.UserHeightResponse
import com.fitnessapp.utils.ServiceResult

class GetUserHeight (private val userHeightDao: UserHeightDao) {
    suspend operator fun invoke(id: Int): UserHeightResponse {
        return when (val userHeight = userHeightDao.getUserHeight(id)) {
            is ServiceResult.Success -> UserHeightResponse(userHeight.data, emptyList())
            is ServiceResult.Error -> UserHeightResponse(null,
                listOf(ResponseErrors(userHeight.error, userHeight.error.message)))
        }
    }
}