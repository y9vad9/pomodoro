package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.RemoveTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object RemoveTimerRequest {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NotFound : Result
    }
}

fun Route.removeTimer(removeTimer: RemoveTimerUseCase) {
    delete {
        authorized { userId ->
            val timerId = call.request.queryParameters.getOrFail("id").toInt()
            val result =
                removeTimer(userId, TimersRepository.TimerId(timerId))

            val response = when (result) {
                is RemoveTimerUseCase.Result.Success -> RemoveTimerRequest.Result.Success
                is RemoveTimerUseCase.Result.NotFound -> RemoveTimerRequest.Result.NotFound
            }

            call.respond(response)
        }
    }
}