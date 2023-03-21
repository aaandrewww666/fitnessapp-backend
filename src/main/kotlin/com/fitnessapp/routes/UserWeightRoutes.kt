package com.fitnessapp.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.fitnessapp.data.UserWeightImpl
import com.fitnessapp.domain.usecase.DeleteUserWeightByDate
import com.fitnessapp.domain.usecase.GetUserWeights
import com.fitnessapp.domain.usecase.InsertUserWeight
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Routing.userWeightRoutes(
    userWeightImpl: UserWeightImpl
) {
    authenticate {
        post("user/weight") {
            //post-запрос на добавление записи о весе пользователя в таблицу usersweight

            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", Int::class)!!

            val userWeight = call.receive<Double>()//получение веса

            val result = InsertUserWeight(userWeightImpl).invoke(
                userId,
                userWeight
            ) //получение ответа о выполнении запроса о результатах добавления

            val httpStatus = if (result.errors.isEmpty()) HttpStatusCode.Created else result.errors
                .first().error.statusCode //код ответа

            call.respond(status = httpStatus, message = result) //возврат кода и сообщения клиенту
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

            val httpStatus = if (result.errors.isEmpty()) HttpStatusCode.Created else result.errors
                .first().error.statusCode

            call.respond(status = httpStatus, message = result)
        }
    }
}