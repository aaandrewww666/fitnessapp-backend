package com.fitnessapp.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

@kotlinx.serialization.Serializable
data class UserData(
    val id: Int = 0,
    val userId: Int,
    val height: Double,
    val gender: Int,
    val age: Int
) //класс, хранящий данные о росте пользователя

object UsersDataTable : IntIdTable("users_data") {
    val userId = integer("user_id")
    val height = double("height")
    val gender = integer("gender")
    val age = integer("age")
}