package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.TimerEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface GetLastEventsResult {
    @Serializable
    @SerialName("success")
    public class Success(public val list: List<TimerEvent>) : GetLastEventsResult

    @Serializable
    @SerialName("no_access")
    public object NoAccess : GetLastEventsResult
}