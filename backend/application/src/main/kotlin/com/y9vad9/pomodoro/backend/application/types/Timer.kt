package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.application.types.value.Name
import com.y9vad9.pomodoro.backend.application.types.value.TimerId
import com.y9vad9.pomodoro.backend.application.types.value.UserId
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Timer(
    @SerialName("timer_id") val timerId: TimerId,
    val name: Name,
    @SerialName("owner_id") val ownerId: UserId,
    val settings: TimerSettings
)

fun TimersRepository.Timer.serializable() = Timer(
    timerId.serializable(),
    name.serializable(),
    ownerId.serializable(),
    settings.serializable()
)