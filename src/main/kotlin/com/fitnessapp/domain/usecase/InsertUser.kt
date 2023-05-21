package com.fitnessapp.domain.usecase

import com.fitnessapp.data.models.User
import com.fitnessapp.data.UserDao
import com.fitnessapp.utils.ErrorCode
import com.fitnessapp.utils.ServiceResult

class InsertUser(private val userDao: UserDao){
    suspend operator fun invoke(userInf : User): ErrorCode? {
        return when (val validation = ValidateUser().invoke(userInf)) {
            is ServiceResult.Success -> {
                when (val response = userDao.insertUser(userInf)) {
                    is ServiceResult.Success -> null
                    is ServiceResult.Error -> response.error
                }
            }
            is ServiceResult.Error -> validation.error
        }
    }
}