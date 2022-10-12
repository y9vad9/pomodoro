package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.NewSettings
import com.y9vad9.pomodoro.backend.application.types.toInternal
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.SetTimerSettingsUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class SetSettingsRequest(
    val timerId: Int,
    val settings: NewSettings,
) {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}

fun Route.setSettings(setSettings: SetTimerSettingsUseCase) {
    patch<SetSettingsRequest> { data ->
        authorized { userId ->
            val result = setSettings(
                userId, TimersRepository.TimerId(data.timerId),
                data.settings.toInternal()
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