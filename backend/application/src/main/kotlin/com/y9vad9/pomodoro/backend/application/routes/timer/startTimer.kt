package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.StartTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object StartTimerRequest {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}

fun Route.startTimer(startTimer: StartTimerUseCase) {
    post("start") { data ->
        val timerId = call.request.queryParameters.getOrFail("id").toInt()
        authorized { userId ->
            val result = startTimer(userId, TimersRepository.TimerId(timerId))

            val response = when (result) {
                is StartTimerUseCase.Result.Success ->
                    StartTimerRequest.Result.Success

                is StartTimerUseCase.Result.NoAccess ->
                    StartTimerRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}