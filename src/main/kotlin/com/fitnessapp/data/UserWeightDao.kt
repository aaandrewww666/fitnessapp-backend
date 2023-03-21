package com.fitnessapp.data

import com.fitnessapp.data.models.UserWeight
import com.fitnessapp.utils.ServiceResult

interface UserWeightDao {
    suspend fun getUserWeights(id : Int) : ServiceResult<List<UserWeight>>
    suspend fun insertUserWeight(userId : Int, weight : Double) : ServiceResult<UserWeight>
    suspend fun deleteUserWeights(id: Int) : ServiceResult<Boolean>
    suspend fun deleteUserWeightByDate(id : Int, date : String) : ServiceResult<Boolean>
}