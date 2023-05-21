package com.fitnessapp.domain.usecase

import com.fitnessapp.data.UserDao
import com.fitnessapp.data.UserDataDao
import com.fitnessapp.data.UserWeightDao
import com.fitnessapp.domain.responses.DeleteResponse
import com.fitnessapp.domain.responses.ResponseErrors
import com.fitnessapp.utils.ServiceResult

class DeleteUser(private val userDao: UserDao, private val userDataDao: UserDataDao, private val userWeightDao: UserWeightDao) {
    suspend operator fun invoke(id : Int): DeleteResponse {
        return when (val resultHeightDelete = userDataDao.deleteUserData(id)) {
            is ServiceResult.Error -> DeleteResponse(
                errors = listOf(
                    ResponseErrors(
                        resultHeightDelete.error
                    )
                )
            )
            is ServiceResult.Success -> return when (val resultWeightDelete = userWeightDao.deleteUserWeights(id)) {
                is ServiceResult.Error -> DeleteResponse(
                    errors = listOf(
                        ResponseErrors(
                            resultWeightDelete.error
                        )
                    )
                )
                is ServiceResult.Success -> return when (val result = userDao.deleteUser(id)) {
                    is ServiceResult.Success -> DeleteResponse(success = true)
                    is ServiceResult.Error -> DeleteResponse(
                        errors = listOf(
                            ResponseErrors(
                                result.error
                            )
                        )
                    )
                }
            }
        }
    }
}