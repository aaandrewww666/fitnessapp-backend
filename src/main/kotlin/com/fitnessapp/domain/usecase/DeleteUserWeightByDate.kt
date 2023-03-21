package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserWeightDao
import com.fitnessapp.domain.responses.DeleteResponse
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.utils.ServiceResult

class DeleteUserWeightByDate (private val userWeightDao: UserWeightDao) {
    suspend operator fun invoke(id: Int, date: String): DeleteResponse {
        return when (val result = userWeightDao.deleteUserWeightByDate(id, date)) {
            is ServiceResult.Success -> DeleteResponse(success = true)
            is ServiceResult.Error -> DeleteResponse(
                errors = listOf(
                    ResponseErrors(
                        result.error,
                        result.error.message
                    )
                )
            )
        }
    }
}