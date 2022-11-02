package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface RemoveTimerResult {
    @SerialName("success")
    public object Success : RemoveTimerResult

    @SerialName("not_found")
    public object NotFound : RemoveTimerResult
}