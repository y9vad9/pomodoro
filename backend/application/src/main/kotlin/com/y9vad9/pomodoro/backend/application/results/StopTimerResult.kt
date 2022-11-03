package com.y9vad9.pomodoro.backend.application.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface StopTimerResult {
    @Serializable
    @SerialName("success")
    object Success : StopTimerResult

    @Serializable
    @SerialName("no_access")
    object NoAccess : StopTimerResult
}