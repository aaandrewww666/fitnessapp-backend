package com.fitnessapp.routes

import com.fitnessapp.data.UserImpl
import com.fitnessapp.data.models.User

import com.fitnessapp.domain.requests.SignInRequest
import com.fitnessapp.domain.requests.SignUpRequest
import com.fitnessapp.domain.responses.MessageResponse
import com.fitnessapp.domain.usecase.GetUserByLogin
import com.fitnessapp.domain.usecase.InsertUser
import com.fitnessapp.security.hashing.HashingService
import com.fitnessapp.security.hashing.SaltedHash
import com.fitnessapp.security.token.TokenClaim
import com.fitnessapp.security.token.TokenConfig
import com.fitnessapp.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.singUp(
    hashingService: HashingService,
    userImpl: UserImpl
) {
    //роутинг с регистрацией пользователя
    post("/user/singup") {
        val request = call.receive<SignUpRequest>()
        val areFieldsBlank = request.userLogin.isNotEmpty()  &&
                request.password.isNotEmpty() &&
                request.username.isNotEmpty()

        val isPwTooShort = request.password.length < 8
        if(!areFieldsBlank) {
            call.respond(HttpStatusCode.NoContent, MessageResponse("Заполните поля"))
            return@post
        }
        if (isPwTooShort) {
            call.respond(HttpStatusCode.LengthRequired, MessageResponse("Короткий пароль"))
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

        if(result == null) {
            call.respond(
                message = MessageResponse("Успешно"),
                status = HttpStatusCode.Created,
            )
            return@post
        }

        call.respond(
            message = MessageResponse(result.message),
            status = result.statusCode
        )
    }
}

fun Routing.singIn(
    hashingService: HashingService,
    tokenService: TokenService,
    userImpl: UserImpl,
    tokenConfig: TokenConfig
) {
    //роутинг с авторизацией пользователя
    post("/user/singin") {
        val request = call.receive<SignInRequest>()
        val areFieldsBlank = request.userLogin.isNotEmpty()  &&
                request.password.isNotEmpty()
        if(!areFieldsBlank) {
            call.respond(HttpStatusCode.NoContent, MessageResponse("Заполните поля"))
            return@post
        }
        val user = GetUserByLogin(userImpl).invoke(request.userLogin).user
        if(user == null) {
            call.respond(HttpStatusCode.NotFound, MessageResponse("Пройдите регистрацию"))
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
            call.respond(HttpStatusCode.Forbidden, MessageResponse("Неверный пароль"))
            return@post
        }
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id
            )
        )
        call.respond(HttpStatusCode.OK, MessageResponse(token))
    }
    authenticate {
        get("authentication/check") {
            try {
                val principal = call.principal<JWTPrincipal>()
                principal?.getClaim("userId", Int::class)!!
                call.respond(
                    message = MessageResponse("Успешно"),
                    status = HttpStatusCode.OK,
                )
            } catch (e: Exception) {
                call.respond(
                    message = MessageResponse(e.message.toString()),
                    status = HttpStatusCode.OK,
                )
            }

        }
    }
}