package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.domain.DateTime
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Milliseconds(val long: Long)

fun com.y9vad9.pomodoro.backend.domain.Milliseconds.serializable() = Milliseconds(long)
fun DateTime.serializable() = Milliseconds(long)

fun Milliseconds.internal() = com.y9vad9.pomodoro.backend.domain.Milliseconds(long)