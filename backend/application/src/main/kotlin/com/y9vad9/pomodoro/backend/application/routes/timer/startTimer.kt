package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.StartTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class StartTimerRequest(
    val timerId: Int
) {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}

fun Route.startTimer(startTimer: StartTimerUseCase) {
    post<StartTimerRequest>("start") { data ->
        authorized { userId ->
            val result = startTimer(userId, TimersRepository.TimerId(data.timerId))

            val response = when (result) {
                is StartTimerUseCase.Result.Success ->
                    SetSettingsRequest.Result.Success

                is StartTimerUseCase.Result.NoAccess ->
                    SetSettingsRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}