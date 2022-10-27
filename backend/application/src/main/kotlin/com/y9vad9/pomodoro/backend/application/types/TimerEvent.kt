package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimerEvent {
    val id: Long
    val startedAt: Long
    val finishesAt: Long?

    @SerialName("START")
    class Started(
        override val id: Long,
        override val startedAt: Long,
        override val finishesAt: Long
    ) : TimerEvent

    @SerialName("STOP")
    class Paused(
        override val id: Long,
        override val startedAt: Long,
        override val finishesAt: Long?
    ) : TimerEvent
}

fun TimersRepository.TimerEvent.toExternal(): TimerEvent {
    return when (this) {
        is TimersRepository.TimerEvent.Started -> TimerEvent.Started(
            id.long, startedAt.long, finishesAt.long
        )

        is TimersRepository.TimerEvent.Paused -> TimerEvent.Paused(
            id.long, startedAt.long, finishesAt?.long
        )
    }
}