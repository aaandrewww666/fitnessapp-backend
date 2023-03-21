package com.fitnessapp.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.fitnessapp.data.UserHeightImpl
import com.fitnessapp.domain.usecase.GetUserHeight
import com.fitnessapp.domain.usecase.InsertUserHeight
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Routing.userHeightRoutes(
    userHeightImpl: UserHeightImpl
) {
    authenticate {
        get("user/height") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val result = GetUserHeight(userHeightImpl).invoke(userId)

            val httpsStatus = if (result.errors.isEmpty()) HttpStatusCode.OK
            else result.errors.first().error.statusCode

            call.respond(
                message = result,
                status = httpsStatus,
            )
        }
    }
    authenticate {
        post("/user/height"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val userHeight = call.receive<Double>()//получение роста

            val result = InsertUserHeight(userHeightImpl).invoke(userId, userHeight)

            val httpStatus = if (result.errors.isEmpty()) HttpStatusCode.Created else result.errors
                .first().error.statusCode

            call.respond(
                status = httpStatus,
                message = result
            )
        }
    }
}