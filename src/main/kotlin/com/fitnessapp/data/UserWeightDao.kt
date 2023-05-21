package com.fitnessapp.data

import com.fitnessapp.data.models.UserWeight
import com.fitnessapp.domain.requests.WeightRequest
import com.fitnessapp.utils.ServiceResult

interface UserWeightDao {
    //объект доступа к работе с таблицей users_weights
    suspend fun getUserWeights(id : Int) : ServiceResult<List<UserWeight>>
    suspend fun insertUserWeight(userId : Int, weight : WeightRequest) : ServiceResult<UserWeight>
    suspend fun deleteUserWeights(id: Int) : ServiceResult<Boolean>
    suspend fun deleteUserWeightByDate(id : Int, date : String) : ServiceResult<Boolean>
}