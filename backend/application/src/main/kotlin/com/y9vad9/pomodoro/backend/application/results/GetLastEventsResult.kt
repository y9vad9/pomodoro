package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.TimerEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GetLastEventsResult {
    @Serializable
    @SerialName("success")
    class Success(val list: List<TimerEvent>) : GetLastEventsResult

    @Serializable
    @SerialName("no_access")
    object NoAccess : GetLastEventsResult
}