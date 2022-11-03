package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.StopTimerResult
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.StopTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.stopTimer(stopTimer: StopTimerUseCase) {
    post("stop") {
        val timerId = call.request.queryParameters.getOrFail("id").toInt()
        authorized { userId ->

            val response = when (stopTimer(userId, TimersRepository.TimerId(timerId))) {
                is StopTimerUseCase.Result.Success ->
                    StopTimerResult.Success

                is StopTimerUseCase.Result.NoAccess ->
                    StopTimerResult.NoAccess
            }

            call.respond(response)
        }
    }
}