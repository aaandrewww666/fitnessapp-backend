package com.fitnessapp.utils

import io.ktor.http.*

sealed class ServiceResult<out T> {
    data class Success<out T>(val data: T?): ServiceResult<T>()
    data class Error(val error: ErrorCode): ServiceResult<Nothing>()
}

enum class ErrorCode(
    val message: String,
    val statusCode: HttpStatusCode
    ) {
    NOT_FOUND("Не найдено", HttpStatusCode.NotFound),
    DATABASE_ERROR("Ошибка базы данных", HttpStatusCode.InternalServerError),
    BADLY_FORMATTED_INPUT("Неверный формат данных", HttpStatusCode.UnprocessableEntity),
    EMPTY_FIELDS("Заполните поля", HttpStatusCode.BadRequest),
    SHORT_PASSWORD("Короткий пароль", HttpStatusCode.Conflict),
    INVALID_PASSWORD("Неверный пароль", HttpStatusCode.Conflict),
    INVALID_EMAIL("Неверный формат почты", HttpStatusCode.BadRequest),
    EMAIL_ALREADY_EXISTS("Пользователь с такой почтой уже существует", HttpStatusCode.Conflict),
    DATA_ALREADY_FILLED("Данные были введёны", HttpStatusCode.Conflict),
    WEIGHT_ALREADY_FILLED("Вес был введён в этот день", HttpStatusCode.Conflict)
}