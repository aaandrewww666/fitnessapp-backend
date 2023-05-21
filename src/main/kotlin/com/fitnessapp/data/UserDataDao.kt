package com.fitnessapp.data

import com.fitnessapp.data.models.UserData
import com.fitnessapp.utils.ServiceResult

interface UserDataDao {
    //объект доступа к работе с таблицей users_height
    suspend fun getUserData(userId : Int) : ServiceResult<UserData>
    suspend fun updateUserAge(userId: Int, userHeight: Double, userGender: Int, userAge: Int) : ServiceResult<Boolean>
    suspend fun insertUserData(userId: Int, userHeight: Double, userGender: Int, userAge: Int) : ServiceResult<Boolean>
    suspend fun deleteUserData(id: Int) : ServiceResult<Boolean>
}