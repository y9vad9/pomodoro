package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.domain.DateTime
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Milliseconds(val long: Long)

fun DateTime.serializable() = Milliseconds(long)