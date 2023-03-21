package com.fitnessapp.data

import com.fitnessapp.data.models.UserHeight
import com.fitnessapp.utils.ServiceResult

interface UserHeightDao {
    suspend fun getUserHeight(id : Int) : ServiceResult<UserHeight>
    suspend fun insertUserHeight(id : Int, userHeight : Double) : ServiceResult<UserHeight>
    suspend fun deleteUserHeight(id: Int) : ServiceResult<Boolean>
}