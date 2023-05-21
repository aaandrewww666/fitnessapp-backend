package com.fitnessapp.plugins

import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    //настройка сериализации
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
}
