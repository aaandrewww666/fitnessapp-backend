package com.fitnessapp.utils

import io.ktor.http.*

sealed class ServiceResult <out T> {
    data class Success<out T>(val data: T): ServiceResult<T>()
    data class Error(val error: ErrorCode): ServiceResult<Nothing>()
}

enum class ErrorCode(val message: String, val statusCode: HttpStatusCode) {
    NOT_FOUND("Not found in database", HttpStatusCode.NotFound),
    DATABASE_ERROR("Database error occurred", HttpStatusCode.InternalServerError),
    BADLY_FORMATTED_INPUT("Fill in correct input data!", HttpStatusCode.BadRequest),
    EMPTY_FIELDS("Fill the fields", HttpStatusCode.BadRequest),
    SHORT_PASSWORD("Make password longer", HttpStatusCode.Conflict),
    INVALID_PASSWORD("Invalid password", HttpStatusCode.Conflict),
    INVALID_EMAIL("Input well formatted email", HttpStatusCode.BadRequest),
    EMAIL_ALREADY_EXISTS("Such email belongs to another user", HttpStatusCode.BadRequest),
    HEIGHT_ALREADY_FILLED("Height was filled", HttpStatusCode.Found),
    WEIGHT_ALREADY_FILLED("Weight was filled for today", HttpStatusCode.Found)
}