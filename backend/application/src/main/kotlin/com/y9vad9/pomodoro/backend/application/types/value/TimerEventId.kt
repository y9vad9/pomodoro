package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class TimerEventId(val long: Long)

fun TimersRepository.TimerEvent.TimerEventId.serializable() = TimerEventId(long)