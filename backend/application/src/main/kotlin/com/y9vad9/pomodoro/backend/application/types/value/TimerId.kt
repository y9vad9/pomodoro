package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class TimerId(val int: Int)

fun TimersRepository.TimerId.serializable() = TimerId(int)
fun TimerId.internal() = TimersRepository.TimerId(int)