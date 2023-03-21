package com.fitnessapp.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {
    fun init(config: DatabaseConfig) {
        Database.connect(
            url = "jdbc:mysql://${config.host}:${config.port}/${config.schema}",
            driver = "com.mysql.cj.jdbc.Driver",
            user = config.user,
            password = config.password)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}