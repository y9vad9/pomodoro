package com.y9vad9.pomodoro.backend.application.routes.timer.events

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.GetEventUpdatesUseCase
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Route.eventUpdates(
    getEventUpdates: GetEventUpdatesUseCase
) {
    webSocket("/updates") {
        authorized { userId ->
            val result = getEventUpdates(
                userId,
                TimersRepository.TimerId(receiveDeserialized()),
                TimersRepository.TimerEvent.TimerEventId(receiveDeserialized())
            )

            when (result) {
                is GetEventUpdatesUseCase.Result.NoAccess -> close(
                    CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No Access")
                )

                is GetEventUpdatesUseCase.Result.Success -> result.flow.collect {
                    sendSerialized(it.serializable())
                }
            }
        }
    }
}