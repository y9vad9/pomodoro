package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.application.types.value.Milliseconds
import com.y9vad9.pomodoro.backend.application.types.value.Regularity
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimerSettings(
    @SerialName("work_time") val workTime: Milliseconds = Milliseconds(1500000L),
    @SerialName("rest_time") val restTime: Milliseconds = Milliseconds(300000),
    @SerialName("big_rest_time") val bigRestTime: Milliseconds = Milliseconds(600000),
    @SerialName("is_big_rest_enabled") val isBigRestEnabled: Boolean = true,
    @SerialName("big_rest_per") val bigRestPer: Regularity = Regularity(4),
    @SerialName("is_everyone_can_pause") val isEveryoneCanPause: Boolean = false
) {
    @Serializable
    class Patch(
        @SerialName("work_time") val workTime: Milliseconds? = null,
        @SerialName("rest_time") val restTime: Milliseconds? = null,
        @SerialName("big_rest_time") val bigRestTime: Milliseconds? = null,
        @SerialName("is_big_rest_enabled") val isBigRestEnabled: Boolean? = null,
        @SerialName("big_rest_per") val bigRestPer: Regularity? = null,
        @SerialName("is_everyone_can_pause") val isEveryoneCanPause: Boolean? = null
    )
}

fun TimersRepository.Settings.serializable() = TimerSettings(
    Milliseconds(workTime), Milliseconds(restTime), Milliseconds(bigRestTime),
    bigRestEnabled, Regularity(bigRestPer), isEveryoneCanPause
)

fun TimersRepository.NewSettings.serializable(): TimerSettings.Patch =
    TimerSettings.Patch(
        workTime?.let { Milliseconds(it) },
        restTime?.let { Milliseconds(it) },
        bigRestTime?.let { Milliseconds(it) },
        bigRestEnabled,
        bigRestPer?.let { Regularity(it) },
        isEveryoneCanPause
    )

fun TimerSettings.internal() = TimersRepository.Settings(
    workTime.long,
    restTime.long,
    bigRestTime.long,
    isBigRestEnabled,
    bigRestPer.int,
    true,
    isEveryoneCanPause
)

fun TimerSettings.Patch.internal() = TimersRepository.NewSettings(
    workTime?.long,
    restTime?.long,
    bigRestTime?.long,
    isBigRestEnabled,
    bigRestPer?.int,
    isEveryoneCanPause
)