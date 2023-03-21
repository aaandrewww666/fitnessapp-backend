package com.fitnessapp.data.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

import java.time.LocalDate

@kotlinx.serialization.Serializable
data class UserWeight(
    val userId: Int,
    val weight : Double,
    val date : String
)

object  UsersWeightTable : Table("users_weight") {
    val userId: Column<Int> = integer("userId")
    val weight: Column<Double> = double("weight")
    val date = date("date").clientDefault { LocalDate.now() }

    override val primaryKey = PrimaryKey(userId, date)
}