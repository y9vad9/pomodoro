package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.Timer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface GetTimerResult {
    @Serializable
    @SerialName("success")
    public class Success(public val timer: Timer) : GetTimerResult

    @Serializable
    @SerialName("not_found")
    public object NotFound : GetTimerResult
}