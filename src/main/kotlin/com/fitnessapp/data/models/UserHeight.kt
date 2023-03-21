package com.fitnessapp.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

@kotlinx.serialization.Serializable
data class UserHeight(
    val id : Int,
    val userId : Int,
    val height : Double
)

object UsersHeightTable : IntIdTable("users_height") {
    val userId = integer("user_id")
    val userHeight = double("height")
}