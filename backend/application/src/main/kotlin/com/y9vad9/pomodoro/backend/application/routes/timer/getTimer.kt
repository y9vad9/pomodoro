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
import kotlinx.serialization.Serializable

@Serializable
data class GetTimerRequest(
    val timerId: Int
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val timer: Timer) : Result
        object NotFound : Result
    }
}

fun Route.getTimer(getTimer: GetTimerUseCase) {
    get {
        authorized { userId ->
            val data = call.receive<GetTimerRequest>()
            val result =
                getTimer(userId, TimersRepository.TimerId(data.timerId))

            val response = when (result) {
                is GetTimerUseCase.Result.Success -> GetTimerRequest.Result.Success(
                    result.timer.toExternal()
                )

                is GetTimerUseCase.Result.NotFound -> GetTimerRequest.Result.NotFound
            }

            call.respond(response)
        }
    }
}