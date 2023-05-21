package com.fitnessapp.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.fitnessapp.data.UserDataImpl
import com.fitnessapp.domain.requests.DataRequest
import com.fitnessapp.domain.responses.DataResponse
import com.fitnessapp.domain.responses.MessageResponse
import com.fitnessapp.domain.usecase.GetUserData
import com.fitnessapp.domain.usecase.InsertUserData
import com.fitnessapp.domain.usecase.UpdateUserData
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Routing.userDataRoutes(
    userDataImpl: UserDataImpl
) {
    //роутинги получения, добавления записей о росте пользователя
    authenticate {
        get("user/data") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val result = GetUserData(userDataImpl).invoke(userId)

            if (result.userData == null && result.error != null) {
                call.respond(
                    message = MessageResponse(result.error.message),
                    status = result.error.statusCode
                )
                return@get
            }

            if (result.userData != null && result.error == null) {
                call.respond(
                    message = DataResponse(
                        height = result.userData.height,
                        age = result.userData.age,
                        gender = result.userData.gender
                    ),
                    status = HttpStatusCode.OK
                )
                return@get
            }
            call.respond(message = MessageResponse("Что-то не так"),
                status = HttpStatusCode.BadRequest
            )

        }
    }

    authenticate {
        put("/user/data") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!
            val userData = call.receive<DataRequest>()
            val result = UpdateUserData(userDataImpl).invoke(userId, userData)
            if(result == null) {
                call.respond(
                    message = MessageResponse("Успешно"),
                    status = HttpStatusCode.OK,
                )
                return@put
            }
            call.respond(
                message = MessageResponse(result.message),
                status = result.statusCode
            )
        }
    }

    authenticate {
        post("/user/data"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val userData = call.receive<DataRequest>()

            val result = InsertUserData(userDataImpl).invoke(userId, userData)

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
}