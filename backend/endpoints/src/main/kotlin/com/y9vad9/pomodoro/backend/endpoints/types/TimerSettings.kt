package com.y9vad9.pomodoro.backend.endpoints.types

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import kotlinx.serialization.Serializable

@Serializable
class TimerSettings(
    @QueryParam("What time will user work")
    val workTime: Long = 1500000L,
    @QueryParam("What time will user rest")
    val restTime: Long = 300000,
    @QueryParam("What time will user rest after several approaches")
    val bigRestTime: Long = 600000,
    @QueryParam("Is big rest per [bigRestPer] enabled")
    val bigRestEnabled: Boolean = true,
    @QueryParam("Per what times will users have big rest")
    val bigRestPer: Int = 4,
    val isEveryoneCanPause: Boolean = false
)