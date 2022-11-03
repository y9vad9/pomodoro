package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.Timer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface GetTimersResult {
    @Serializable
    @SerialName("success")
    @JvmInline
    value class Success(val list: List<Timer>) : GetTimersResult
}