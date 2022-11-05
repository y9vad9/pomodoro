package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.GetTimersResult
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.usecases.timers.GetTimersUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.getTimers(getTimers: GetTimersUseCase) {
    get("all") {
        authorized { userId ->
            val count = call.request.queryParameters.getOrFail("count").toInt()
            val offset = call.request.queryParameters.getOrFail("offset").toInt()
            val result: GetTimersResult =
                GetTimersResult.Success(
                    (getTimers(
                        userId,
                        offset..count + offset
                    ) as GetTimersUseCase.Result.Success).list.map { it.serializable() }
                )

            call.respond(result)
        }
    }
}