package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.TimerSettings
import com.y9vad9.pomodoro.backend.application.types.toInternal
import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.CreateTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTimerRequest(
    val name: String,
    val settings: TimerSettings,
) {
    @Serializable
    sealed interface Result {
        @SerialName("success")
        @JvmInline
        value class Success(val timerId: TimersRepository.TimerId) : Result
    }
}

fun Route.createTimer(createTimer: CreateTimerUseCase) {
    post<CreateTimerRequest> { data ->
        authorized { userId ->
            val result: CreateTimerRequest.Result =
                when (val result = createTimer(
                    userId, data.settings.toInternal(), TimerName(data.name)
                )) {
                    is CreateTimerUseCase.Result.Success ->
                        CreateTimerRequest.Result.Success(result.timerId)
                }

            call.respond(result)
        }
    }
}