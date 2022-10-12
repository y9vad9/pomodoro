package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.serialization.Serializable

@Serializable
class Timer(
    val timerId: Int,
    val name: String,
    val ownerId: Int,
    val settings: TimerSettings
)

fun Timer.toInternal(): TimersRepository.Timer {
    return TimersRepository.Timer(
        TimersRepository.TimerId(timerId),
        TimerName(name),
        UsersRepository.UserId(ownerId),
        settings.toInternal()
    )
}

fun TimersRepository.Timer.toExternal(): Timer {
    return Timer(
        timerId.int,
        name.string,
        ownerId.int,
        settings.toExternal()
    )
}