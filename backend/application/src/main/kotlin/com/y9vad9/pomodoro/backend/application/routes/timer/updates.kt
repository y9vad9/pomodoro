package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.TimerSessionCommand
import com.y9vad9.pomodoro.backend.application.types.TimerUpdate
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.application.types.value.TimerId
import com.y9vad9.pomodoro.backend.application.types.value.internal
import com.y9vad9.pomodoro.backend.usecases.timers.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun Route.timerUpdates(
    joinSessionUseCase: JoinSessionUseCase,
    leaveSessionUseCase: LeaveSessionUseCase,
    confirmStartUseCase: ConfirmStartUseCase,
    startTimerUseCase: StartTimerUseCase,
    stopTimerUseCase: StopTimerUseCase
) {
    webSocket("track") {
        authorized { userId ->
            val timerId = call.request.queryParameters
                .getOrFail("timer_id")
                .toIntOrNull()
                ?.let { TimerId(it).internal() }

            if (timerId == null) {
                sendSerialized(TimerUpdate.SessionFinished)
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "timer_id is invalid."))
                return@webSocket
            }

            try {

                val joinResult = joinSessionUseCase(userId, timerId)
                if (joinResult !is JoinSessionUseCase.Result.Success) {
                    sendSerialized(TimerUpdate.SessionFinished)
                    close(CloseReason(CloseReason.Codes.GOING_AWAY, "Join failed."))
                    return@webSocket
                }

                val updatesJob = launch {
                    joinResult.updates.collectLatest {
                        sendSerialized(it.serializable())
                    }
                }

                val commandsJob = launch {
                    while (isActive) {
                        when (receiveDeserialized<TimerSessionCommand>()) {
                            is TimerSessionCommand.StartTimer -> startTimerUseCase(userId, timerId)
                            is TimerSessionCommand.ConfirmAttendance -> confirmStartUseCase(userId, timerId)
                            is TimerSessionCommand.StopTimer -> stopTimerUseCase(userId, timerId)
                            is TimerSessionCommand.LeaveSession -> {
                                leaveSessionUseCase(userId, timerId)
                                close(CloseReason(CloseReason.Codes.GOING_AWAY, "Session end."))
                            }
                        }
                    }
                }

                updatesJob.join()
                commandsJob.join()
            } catch (e: Exception) {
                leaveSessionUseCase(userId, timerId)
                e.printStackTrace()
            }
        }
    }
}
