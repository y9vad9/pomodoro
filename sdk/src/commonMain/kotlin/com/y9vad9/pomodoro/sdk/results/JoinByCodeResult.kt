package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.TimerId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface JoinByCodeResult {
    @Serializable
    @SerialName("success")
    public class Success(
        @SerialName("timer_id")
        public val timerId: TimerId
    ) : JoinByCodeResult

    @Serializable
    @SerialName("not_found")
    public object NotFound : JoinByCodeResult
}