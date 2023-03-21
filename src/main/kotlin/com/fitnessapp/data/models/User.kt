package com.fitnessapp.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

@kotlinx.serialization.Serializable
data class User(
    val id: Int = 0,
    val userLogin : String,
    val username: String,
    val password: String,
    val salt: String
)

object UsersTable : IntIdTable("users") {
    val login = varchar("login",45)
    val password = varchar("password",64)
    val salt = varchar("salt", 64)
    val username = varchar("username",45)
}