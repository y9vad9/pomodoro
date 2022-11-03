package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.CreateTimerResult
import com.y9vad9.pomodoro.backend.application.types.TimerSettings
import com.y9vad9.pomodoro.backend.application.types.internal
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.usecases.timers.CreateTimerUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateTimerRequest(
    val name: String,
    val settings: TimerSettings,
)

fun Route.createTimer(createTimer: CreateTimerUseCase) {
    post<CreateTimerRequest> { data ->
        authorized { userId ->
            val result: CreateTimerResult =
                when (val result = createTimer(
                    userId, data.settings.internal(), TimerName(data.name)
                )) {
                    is CreateTimerUseCase.Result.Success ->
                        CreateTimerResult.Success(result.timerId.serializable())
                }

            call.respond(result)
        }
    }
}