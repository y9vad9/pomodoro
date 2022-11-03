package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.Timer
import com.y9vad9.pomodoro.backend.application.types.toExternal
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.GetTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object GetTimerRequest {
    @Serializable
    sealed interface Result {
        @SerialName("success")
        @JvmInline
        value class Success(val timer: Timer) : Result

        @SerialName("not_found")
        object NotFound : Result
    }
}

fun Route.getTimer(getTimer: GetTimerUseCase) {
    get {
        authorized { userId ->
            val timerId = call.request.queryParameters.getOrFail("id").toInt()

            val response = when (
                val result = getTimer(userId, TimersRepository.TimerId(timerId))
            ) {
                is GetTimerUseCase.Result.Success -> GetTimerRequest.Result.Success(
                    result.timer.toExternal()
                )

                is GetTimerUseCase.Result.NotFound -> GetTimerRequest.Result.NotFound
            }

            call.respond(response)
        }
    }
}