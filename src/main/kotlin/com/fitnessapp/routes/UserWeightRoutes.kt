package com.fitnessapp.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.fitnessapp.data.UserWeightImpl
import com.fitnessapp.domain.requests.WeightRequest
import com.fitnessapp.domain.responses.DataResponse
import com.fitnessapp.domain.responses.MessageResponse
import com.fitnessapp.domain.responses.WeightResponse
import com.fitnessapp.domain.usecase.DeleteUserWeightByDate
import com.fitnessapp.domain.usecase.GetUserWeights
import com.fitnessapp.domain.usecase.InsertUserWeight
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Routing.userWeightRoutes(
    userWeightImpl: UserWeightImpl
) {
    //роутинги с получением, удалением и добавлением записей о весе пользователя
    authenticate {
        post("user/weight") {
            //post-запрос на добавление записи о весе пользователя в таблицу usersweight

            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val userWeight = call.receive<WeightRequest>()//получение веса

            val result = InsertUserWeight(userWeightImpl).invoke(
                userId,
                userWeight
            ) //получение ответа о выполнении запроса о результатах добавления

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

    authenticate {
        delete("user/weight/by-date") {
            //delete-запрос на удаление записи об определённом пользователе в определённую дату
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val date = call.receive<String>()

            val result =
                DeleteUserWeightByDate(userWeightImpl).invoke(userId, date) //удаление записи по дате

            val httpStatus = if (result.errors.isEmpty()) HttpStatusCode.Created else result.errors
                .first().error.statusCode

            call.respond(status = httpStatus, message = result)

        }
    }

    authenticate {
        get("user/weight") {
            //get-запрос на получение клиентом списка информации о весе конкретного пользователя
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val result =
                GetUserWeights(userWeightImpl).invoke(userId) //получение ответа о выполнении запроса по получению данных

            if (result.userWeights == null && result.error != null) {
                call.respond(
                    message = MessageResponse(result.error.message),
                    status = result.error.statusCode
                )
                return@get
            }
            if (result.userWeights != null && result.error == null) {
                val response = mutableListOf<WeightRequest>()
                result.userWeights.iterator().forEach {
                    response += WeightRequest(
                        userWeight = it.weight,
                        date = it.date
                    )
                }
                call.respond(
                    message = WeightResponse(
                        userWeights = response
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
}