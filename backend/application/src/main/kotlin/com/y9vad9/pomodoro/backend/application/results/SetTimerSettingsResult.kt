package com.y9vad9.pomodoro.backend.application.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface SetTimerSettingsResult {
    @Serializable
    @SerialName("success")
    object Success : SetTimerSettingsResult

    @Serializable
    @SerialName("no_access")
    object NoAccess : SetTimerSettingsResult
}