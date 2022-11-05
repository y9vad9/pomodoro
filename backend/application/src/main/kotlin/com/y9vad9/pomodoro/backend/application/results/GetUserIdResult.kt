package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.value.UserId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GetUserIdResult {
    @Serializable
    @SerialName("success")
    class Success(@SerialName("user_id") val userId: UserId) : GetUserIdResult
}