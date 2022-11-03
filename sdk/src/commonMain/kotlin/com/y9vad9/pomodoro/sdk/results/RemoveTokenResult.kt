package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public sealed interface RemoveTokenResult {
    @Serializable
    @SerialName("success")
    public object Success : RemoveTokenResult
}