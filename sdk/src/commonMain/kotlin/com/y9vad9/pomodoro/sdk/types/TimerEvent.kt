package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.Milliseconds
import com.y9vad9.pomodoro.sdk.types.value.TimerEventId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface TimerEvent {
    public val id: TimerEventId

    @SerialName("started_at")
    public val startedAt: Milliseconds

    @SerialName("finishes_at")
    public val finishesAt: Milliseconds?

    @Serializable
    @SerialName("start")
    public class Started(
        override val id: TimerEventId,
        override val startedAt: Milliseconds,
        override val finishesAt: Milliseconds
    ) : TimerEvent

    @Serializable
    @SerialName("stop")
    public class Paused(
        override val id: TimerEventId,
        override val startedAt: Milliseconds,
        override val finishesAt: Milliseconds?
    ) : TimerEvent
}