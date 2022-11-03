package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.value.UserId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface GetUserIdResult {
    @Serializable
    @SerialName("success")
    @JvmInline
    value class Success(val userId: UserId) : GetUserIdResult
}