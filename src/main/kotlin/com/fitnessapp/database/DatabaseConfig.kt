package com.fitnessapp.database

sealed class DatabaseConfig(
    val host: String,
    val port: Int,
    val schema: String,
    val user: String,
    val password: String
) {

    object MySqlConfig: DatabaseConfig(
        host = "127.0.0.1",
        port = 3306,
        schema = "diploma",
        user = "fitnessClient",
        password = "JHGfvbhjhgfF#23"
    )

}