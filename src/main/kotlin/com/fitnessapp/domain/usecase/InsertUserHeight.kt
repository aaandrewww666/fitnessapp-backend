package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserHeightImpl
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.domain.responses.UserHeightResponse
import com.fitnessapp.utils.ServiceResult

class InsertUserHeight(private val userHeightDao: UserHeightImpl){
    suspend operator fun invoke(userId : Int, height : Double): UserHeightResponse {
        return when (val userHeight = userHeightDao.insertUserHeight(userId, height)) {
            is ServiceResult.Success -> UserHeightResponse(userHeight.data, emptyList())
            is ServiceResult.Error -> UserHeightResponse(null,
                listOf(ResponseErrors(userHeight.error, userHeight.error.message)))
        }
    }
}