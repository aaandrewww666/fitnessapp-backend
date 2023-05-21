package com.fitnessapp

import com.fitnessapp.data.UserDataImpl
import com.fitnessapp.data.UserImpl
import com.fitnessapp.data.UserWeightImpl
import com.fitnessapp.database.DatabaseConfig
import com.fitnessapp.database.DatabaseFactory
import com.fitnessapp.plugins.configureJWT
import com.fitnessapp.plugins.configureRouting
import com.fitnessapp.plugins.configureSerialization
import com.fitnessapp.security.hashing.SHA256HashingService
import com.fitnessapp.security.token.JwtTokenService
import com.fitnessapp.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO,
        port = 8081,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true) //запуск сервера
}

fun Application.module() {
    configureSerialization()
    DatabaseFactory.init(config = DatabaseConfig.MySqlConfig)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig (
        issuer = System.getenv("ISSUER"), //получение переменной окружения
        audience = System.getenv("AUDIENCE"),
        expiresIn = 60L * 60L * 1000L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureJWT(tokenConfig)
    configureRouting(UserImpl(), UserDataImpl(), UserWeightImpl(), hashingService,tokenConfig,tokenService)
}
