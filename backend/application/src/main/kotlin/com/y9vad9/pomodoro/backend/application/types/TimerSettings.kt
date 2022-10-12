package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.Serializable

@Serializable
class TimerSettings(
    val workTime: Long = 1500000L,
    val restTime: Long = 300000,
    val bigRestTime: Long = 600000,
    val bigRestEnabled: Boolean = true,
    val bigRestPer: Int = 4,
    val isEveryoneCanPause: Boolean = false
)

fun TimerSettings.toInternal(): TimersRepository.Settings = TimersRepository.Settings(
    workTime,
    restTime,
    bigRestTime,
    bigRestEnabled,
    bigRestPer,
    true,
    isEveryoneCanPause
)

fun TimersRepository.Settings.toExternal(): TimerSettings = TimerSettings(
    workTime,
    restTime,
    bigRestTime,
    bigRestEnabled,
    bigRestPer,
    isEveryoneCanPause
)