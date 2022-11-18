package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.application.types.value.Milliseconds
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.repositories.TimerUpdatesRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TimerUpdate {
    @SerialName("confirmation")
    @Serializable
    object Confirmation : TimerUpdate

    @SerialName("settings")
    @Serializable
    class Settings(
        @SerialName("new_settings") val newSettings: TimerSettings.Patch
    ) : TimerUpdate

    @SerialName("started")
    @Serializable
    class TimerStarted(@SerialName("ends_at") val endsAt: Milliseconds) : TimerUpdate

    @SerialName("stopped")
    @Serializable
    class TimerStopped(@SerialName("starts_at") val startsAt: Milliseconds?) : TimerUpdate
}

fun TimerUpdatesRepository.Update.serializable(): TimerUpdate {
    return when (this) {
        is TimerUpdatesRepository.Update.Confirmation -> TimerUpdate.Confirmation
        is TimerUpdatesRepository.Update.TimerStarted -> TimerUpdate.TimerStarted(endsAt.serializable())
        is TimerUpdatesRepository.Update.TimerStopped -> TimerUpdate.TimerStopped(startsAt?.serializable())
        is TimerUpdatesRepository.Update.Settings -> TimerUpdate.Settings(newSettings.serializable())
    }
}