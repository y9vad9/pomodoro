package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface RemoveTokenResult {
    @SerialName("success")
    public object Success : RemoveTokenResult
}