package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.value.TimerId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface CreateTimerResult {
    @Serializable
    @SerialName("success")
    @JvmInline
    value class Success(
        @SerialName("timer_id") val timerId: TimerId
    ) : CreateTimerResult
}