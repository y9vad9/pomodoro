package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.Timer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface GetTimerResult {
    @SerialName("success")
    @JvmInline
    public value class Success(public val timer: Timer) : GetTimerResult

    @SerialName("not_found")
    public object NotFound : GetTimerResult
}