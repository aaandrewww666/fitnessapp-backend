package com.fitnessapp.routes

import com.fitnessapp.data.UserDataImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.fitnessapp.data.UserImpl
import com.fitnessapp.data.UserWeightImpl
import com.fitnessapp.domain.usecase.DeleteUser
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Routing.userRoutes(
    userImpl: UserImpl,
    userWeightImpl: UserWeightImpl,
    userHeightImpl: UserDataImpl
) {
    //роутинги с получением, удалением записей о пользователе
    /*authenticate {
        get("/user") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)

            exposedLogger.info(userId.toString())
            val result = GetUserById(userImpl).invoke(userId!!)

            val httpsStatus = if (result.errors.isEmpty()) HttpStatusCode.OK
            else result.errors.first().error.statusCode

            call.respond(
                message = result,
                status = httpsStatus,
            )

        }
    }*/
    authenticate {
        delete("/user") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)

            val result = DeleteUser(userImpl, userHeightImpl, userWeightImpl).invoke(userId!!)

            val httpsStatus = if (result.errors.isEmpty()) HttpStatusCode.OK
            else result.errors.first().error.statusCode

            call.respond(
                message = result,
                status = httpsStatus,
            )
        }
    }
}