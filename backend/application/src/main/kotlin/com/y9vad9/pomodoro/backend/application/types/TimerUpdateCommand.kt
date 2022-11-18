package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.usecases.timers.GetLiveUpdatesUseCase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TimerUpdateCommand {
    @SerialName("start")
    @Serializable
    object Start : TimerUpdateCommand

    @Serializable
    @SerialName("stop")
    object Stop : TimerUpdateCommand
}

fun TimerUpdateCommand.internal(): GetLiveUpdatesUseCase.Command {
    return when (this) {
        is TimerUpdateCommand.Start -> GetLiveUpdatesUseCase.Command.Start
        is TimerUpdateCommand.Stop -> GetLiveUpdatesUseCase.Command.Stop
    }
}