package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface StopTimerResult {
    @SerialName("success")
    public object Success : StopTimerResult

    @SerialName("no_access")
    public object NoAccess : StopTimerResult
}