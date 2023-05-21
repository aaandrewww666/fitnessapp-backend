package com.fitnessapp.data.models

import kotlinx.serialization.SerialName
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

import java.time.LocalDate

@kotlinx.serialization.Serializable
data class UserWeight(
    @SerialName("userId") val userId: Int,
    @SerialName("weight") val weight : Double,
    @SerialName("date") val date : String
) //класс, хранящий данные о весе пользователя

object  UsersWeightTable : Table("users_weight") {
    val userId: Column<Int> = integer("userId")
    val weight: Column<Double> = double("weight")
    val date = date("date").clientDefault { LocalDate.now() }

    override val primaryKey = PrimaryKey(userId, date)
} //таблица users_weight в БД