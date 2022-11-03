package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public sealed interface StopTimerResult {
    @Serializable
    @SerialName("success")
    public object Success : StopTimerResult

    @Serializable
    @SerialName("no_access")
    public object NoAccess : StopTimerResult
}