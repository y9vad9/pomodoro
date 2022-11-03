package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.StartTimerResult
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.StartTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.startTimer(startTimer: StartTimerUseCase) {
    post("start") {
        val timerId = call.request.queryParameters.getOrFail("id").toInt()
        authorized { userId ->
            val result = startTimer(userId, TimersRepository.TimerId(timerId))

            val response = when (result) {
                is StartTimerUseCase.Result.Success ->
                    StartTimerResult.Success

                is StartTimerUseCase.Result.NoAccess ->
                    StartTimerResult.NoAccess
            }

            call.respond(response)
        }
    }
}