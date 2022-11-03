package com.y9vad9.pomodoro.sdk.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface TimerEvent {
    public val id: Long
    public val startedAt: Long
    public val finishesAt: Long?

    @SerialName("start")
    public class Started(
        override val id: Long,
        override val startedAt: Long,
        override val finishesAt: Long
    ) : TimerEvent

    @SerialName("stop")
    public class Paused(
        override val id: Long,
        override val startedAt: Long,
        override val finishesAt: Long?
    ) : TimerEvent
}