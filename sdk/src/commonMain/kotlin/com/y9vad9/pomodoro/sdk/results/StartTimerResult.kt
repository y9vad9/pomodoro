package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface StartTimerResult {
    @Serializable
    @SerialName("success")
    public object Success : StartTimerResult

    @Serializable
    @SerialName("no_access")
    public object NoAccess : StartTimerResult
}