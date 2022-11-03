package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.TimerId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

public sealed interface CreateTimerResult {
    @Serializable
    @SerialName("success")
    @JvmInline
    public value class Success(
        @SerialName("timer_id")
        public val timerId: TimerId
    ) : CreateTimerResult
}