package com.y9vad9.pomodoro.backend.application.types.value

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class TimerEventId(val long: Long)