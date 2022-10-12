package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.RemoveTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class RemoveTimerRequest(
    val timerId: Int
) {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NotFound : Result
    }
}

fun Route.removeTimer(removeTimer: RemoveTimerUseCase) {
    post<RemoveTimerRequest> { data ->
        authorized { userId ->
            val result =
                removeTimer(userId, TimersRepository.TimerId(data.timerId))

            val response = when (result) {
                is RemoveTimerUseCase.Result.Success -> RemoveTimerRequest.Result.Success
                is RemoveTimerUseCase.Result.NotFound -> RemoveTimerRequest.Result.NotFound
            }

            call.respond(response)
        }
    }
}