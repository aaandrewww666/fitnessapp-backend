package com.fitnessapp.plugins

import com.fitnessapp.data.UserHeightImpl
import io.ktor.server.routing.*
import io.ktor.server.application.*
import com.fitnessapp.data.UserImpl
import com.fitnessapp.data.UserWeightImpl
import com.fitnessapp.routes.*
import com.fitnessapp.security.hashing.HashingService
import com.fitnessapp.security.token.TokenConfig
import com.fitnessapp.security.token.TokenService

fun Application.configureRouting(
    userImpl: UserImpl,
    userHeightImpl: UserHeightImpl,
    userWeightImpl: UserWeightImpl,
    hashingService: HashingService,
    tokenConfig: TokenConfig,
    tokenService: TokenService
) {
    routing {
        userRoutes(userImpl, userWeightImpl, userHeightImpl)
        userHeightRoutes(userHeightImpl)
        userWeightRoutes(userWeightImpl)
        authenticate()
        singIn(hashingService, tokenService, userImpl, tokenConfig)
        singUp(hashingService, userImpl)
    }
}
