package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.GetTimerResult
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.GetTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.getTimer(getTimer: GetTimerUseCase) {
    get {
        authorized { userId ->
            val timerId = call.request.queryParameters.getOrFail("id").toInt()

            val response = when (
                val result = getTimer(userId, TimersRepository.TimerId(timerId))
            ) {
                is GetTimerUseCase.Result.Success -> GetTimerResult.Success(
                    result.timer.serializable()
                )

                is GetTimerUseCase.Result.NotFound -> GetTimerResult.NotFound
            }
            call.respond(response)
        }
    }
}