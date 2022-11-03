package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.TimerId
import com.y9vad9.pomodoro.sdk.types.value.UserId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class Timer(
    @SerialName("timer_id")
    public val timerId: TimerId,
    public val name: String,
    @SerialName("owner_id")
    public val ownerId: UserId,
    public val settings: TimerSettings
)