package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.application.types.value.Milliseconds
import com.y9vad9.pomodoro.backend.application.types.value.TimerEventId
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TimerEvent {
    val id: TimerEventId

    @SerialName("started_at")
    val startedAt: Milliseconds

    @SerialName("finishes_at")
    val finishesAt: Milliseconds?

    @Serializable
    @SerialName("start")
    class Started(
        override val id: TimerEventId,
        override val startedAt: Milliseconds,
        override val finishesAt: Milliseconds
    ) : TimerEvent

    @Serializable
    @SerialName("stop")
    class Paused(
        override val id: TimerEventId,
        override val startedAt: Milliseconds,
        override val finishesAt: Milliseconds?
    ) : TimerEvent
}

fun TimersRepository.TimerEvent.serializable() = when (this) {
    is TimersRepository.TimerEvent.Started -> TimerEvent.Started(
        id.serializable(), startedAt.serializable(), finishesAt.serializable()
    )

    is TimersRepository.TimerEvent.Paused -> TimerEvent.Paused(
        id.serializable(), startedAt.serializable(), finishesAt?.serializable()
    )
}