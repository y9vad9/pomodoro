package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.Timer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GetTimerResult {
    @Serializable
    @SerialName("success")
    class Success(val timer: Timer) : GetTimerResult

    @Serializable
    @SerialName("not_found")
    object NotFound : GetTimerResult
}