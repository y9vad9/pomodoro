package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.StopTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class StopTimerRequest(
    val timerId: Int
) {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}

fun Route.stopTimer(stopTimer: StopTimerUseCase) {
    post<StopTimerRequest>("stop") { data ->
        authorized { userId ->
            val result = stopTimer(userId, TimersRepository.TimerId(data.timerId))

            val response = when (result) {
                is StopTimerUseCase.Result.Success ->
                    SetSettingsRequest.Result.Success

                is StopTimerUseCase.Result.NoAccess ->
                    SetSettingsRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}