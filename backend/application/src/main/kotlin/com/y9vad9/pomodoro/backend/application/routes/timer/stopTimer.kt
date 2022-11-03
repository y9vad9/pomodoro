package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.StopTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object StopTimerRequest {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}

fun Route.stopTimer(stopTimer: StopTimerUseCase) {
    post("stop") {
        val timerId = call.request.queryParameters.getOrFail("id").toInt()
        authorized { userId ->

            val response = when (stopTimer(userId, TimersRepository.TimerId(timerId))) {
                is StopTimerUseCase.Result.Success ->
                    StopTimerRequest.Result.Success

                is StopTimerUseCase.Result.NoAccess ->
                    StopTimerRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}