package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.NewSettings
import com.y9vad9.pomodoro.backend.application.types.toInternal
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.SetTimerSettingsUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object SetSettingsRequest {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}

fun Route.setSettings(setSettings: SetTimerSettingsUseCase) {
    patch<NewSettings> { data ->
        authorized { userId ->
            val timerId = call.request.queryParameters.getOrFail("id").toInt()
            val result = setSettings(
                userId, TimersRepository.TimerId(timerId),
                data.toInternal()
            )

            val response = when (result) {
                is SetTimerSettingsUseCase.Result.Success ->
                    SetSettingsRequest.Result.Success

                is SetTimerSettingsUseCase.Result.NoAccess ->
                    SetSettingsRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}