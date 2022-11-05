package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.RemoveTimerResult
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.RemoveTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.removeTimer(removeTimer: RemoveTimerUseCase) {
    delete {
        authorized { userId ->
            val timerId = call.request.queryParameters.getOrFail("id").toInt()
            val result =
                removeTimer(userId, TimersRepository.TimerId(timerId))

            val response = when (result) {
                is RemoveTimerUseCase.Result.Success -> RemoveTimerResult.Success
                is RemoveTimerUseCase.Result.NotFound -> RemoveTimerResult.NotFound
            }

            call.respond(response)
        }
    }
}