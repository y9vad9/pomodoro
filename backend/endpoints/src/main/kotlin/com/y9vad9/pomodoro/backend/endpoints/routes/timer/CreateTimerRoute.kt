package com.y9vad9.pomodoro.backend.endpoints.routes.timer

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.annotations.type.string.length.MaxLength
import com.y9vad9.pomodoro.backend.endpoints.types.TimerSettings
import com.y9vad9.pomodoro.backend.usecases.timers.CreateTimerUseCase
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateTimerRoute(
    @MaxLength(50, "Max length of name is 50.")
    @QueryParam("Display of the timer")
    val name: String,
    val settings: TimerSettings
)

sealed interface Result

fun Route.createTimerRoute(createTimerUseCase: CreateTimerUseCase) {
    post<CreateTimerUseCase> {

    }
}