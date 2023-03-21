package com.fitnessapp.utils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val FORMAT = "yyyy-MM-dd"

fun LocalDate.toDatabaseString(): String {

    val formatter = DateTimeFormatter.ofPattern(FORMAT)

    return formatter.format(this)
}