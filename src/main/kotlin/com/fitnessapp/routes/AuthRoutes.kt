package com.fitnessapp.routes

import com.fitnessapp.data.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.fitnessapp.data.UserImpl
import com.fitnessapp.domain.requests.SingInRequest
import com.fitnessapp.domain.requests.SingUpRequest
import com.fitnessapp.domain.responses.AuthResponse
import com.fitnessapp.domain.usecase.GetUserByLogin
import com.fitnessapp.domain.usecase.InsertUser
import com.fitnessapp.security.hashing.HashingService
import com.fitnessapp.security.hashing.SaltedHash
import com.fitnessapp.security.token.TokenClaim
import com.fitnessapp.security.token.TokenConfig
import com.fitnessapp.security.token.TokenService
import com.fitnessapp.utils.ErrorCode

fun Routing.singUp(
    hashingService: HashingService,
    userImpl: UserImpl
) {
    post("/user/singup") {
        val request = call.receive<SingUpRequest>()

        val areFieldsBlank = request.userLogin.isNotEmpty()  || request.password.isNotEmpty()
        val isPwTooShort = request.password.length < 8
        if(!areFieldsBlank) {
            call.respond(ErrorCode.EMPTY_FIELDS)
            return@post
        }
        if(isPwTooShort) {
            call.respond(ErrorCode.SHORT_PASSWORD)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)

        val result = InsertUser(userImpl).invoke(
            User(
            userLogin = request.userLogin,
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
            )
        )

        val httpsStatus = if (result.errors.isEmpty()) HttpStatusCode.OK
        else result.errors.first().error.statusCode

        call.respond(
            message = result,
            status = httpsStatus,
        )
    }
}

fun Routing.singIn(
    hashingService: HashingService,
    tokenService: TokenService,
    userImpl: UserImpl,
    tokenConfig: TokenConfig
) {
    post("/user/singin") {
        val request = kotlin.runCatching { call.receiveNullable<SingInRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(ErrorCode.EMPTY_FIELDS)
            return@post
        }

        val user = GetUserByLogin(userImpl).invoke(request.userLogin).user
        if(user == null) {
            call.respond(ErrorCode.NOT_FOUND)
            return@post
        }

        val isValidPassword = hashingService.verify(
            password = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if(!isValidPassword) {
            call.respond(ErrorCode.INVALID_PASSWORD)
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse (
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}