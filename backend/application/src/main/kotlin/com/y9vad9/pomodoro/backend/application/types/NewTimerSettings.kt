package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.Serializable

@Serializable
class NewSettings(
    val workTime: Long? = null,
    val restTime: Long? = null,
    val bigRestTime: Long? = null,
    val bigRestEnabled: Boolean? = null,
    val bigRestPer: Int? = null,
    val isEveryoneCanPause: Boolean? = null
)

fun NewSettings.toInternal(): TimersRepository.NewSettings =
    TimersRepository.NewSettings(
        workTime,
        restTime,
        bigRestTime,
        bigRestEnabled,
        bigRestPer,
        isEveryoneCanPause
    )