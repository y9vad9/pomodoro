package com.y9vad9.pomodoro.sdk.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface TimerEvent {
    public val id: Long

    @SerialName("started_at")
    public val startedAt: Long

    @SerialName("finishes_at")
    public val finishesAt: Long?

    @Serializable
    @SerialName("start")
    public class Started(
        override val id: Long,
        override val startedAt: Long,
        override val finishesAt: Long
    ) : TimerEvent

    @Serializable
    @SerialName("stop")
    public class Paused(
        override val id: Long,
        override val startedAt: Long,
        override val finishesAt: Long?
    ) : TimerEvent
}