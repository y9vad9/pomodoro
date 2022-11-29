package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.DateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public sealed interface TimerUpdate {
    @SerialName("session_timer_confirmation")
    @Serializable
    public object Confirmation : TimerUpdate

    @SerialName("timer_settings_update")
    @Serializable
    public class Settings(
        @SerialName("new_settings") public val patch: TimerSettings.Patch
    ) : TimerUpdate

    @SerialName("timer_started")
    @Serializable
    public class TimerStarted(@SerialName("ends_at") public val endsAt: DateTime) : TimerUpdate

    @SerialName("timer_stopped")
    @Serializable
    public class TimerStopped(@SerialName("starts_at") public val startsAt: DateTime?) : TimerUpdate

    @SerialName("session_failed")
    @Serializable
    public object SessionFailed : TimerUpdate

    @SerialName("session_finished")
    @Serializable
    public object SessionFinished : TimerUpdate
}