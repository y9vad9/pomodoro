package com.y9vad9.pomodoro.backend.application.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface StartTimerResult {
    @Serializable
    @SerialName("success")
    object Success : StartTimerResult

    @Serializable
    @SerialName("no_access")
    object NoAccess : StartTimerResult
}