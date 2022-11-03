package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.SetTimerSettingsResult
import com.y9vad9.pomodoro.backend.application.types.TimerSettings
import com.y9vad9.pomodoro.backend.application.types.internal
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.SetTimerSettingsUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.setSettings(setSettings: SetTimerSettingsUseCase) {
    patch<TimerSettings.Patch> { data ->
        authorized { userId ->
            val timerId = call.request.queryParameters.getOrFail("id").toInt()
            val result = setSettings(
                userId, TimersRepository.TimerId(timerId),
                data.internal()
            )

            val response = when (result) {
                is SetTimerSettingsUseCase.Result.Success ->
                    SetTimerSettingsResult.Success

                is SetTimerSettingsUseCase.Result.NoAccess ->
                    SetTimerSettingsResult.NoAccess
            }

            call.respond(response)
        }
    }
}