package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.TimerId
import kotlinx.serialization.Serializable

@Serializable
public class Timer(
    public val timerId: TimerId,
    public val name: String,
    public val ownerId: Int,
    public val settings: TimerSettings
)