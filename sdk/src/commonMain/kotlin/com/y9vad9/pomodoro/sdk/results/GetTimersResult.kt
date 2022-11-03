package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.Timer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public sealed interface GetTimersResult {
    @Serializable
    @SerialName("success")
    public class Success(public val list: List<Timer>) : GetTimersResult
}