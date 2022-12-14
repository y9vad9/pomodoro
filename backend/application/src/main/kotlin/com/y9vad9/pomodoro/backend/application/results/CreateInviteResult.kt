package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.value.Code
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface CreateInviteResult {
    @Serializable
    @SerialName("success")
    class Success(val code: Code) : CreateInviteResult

    @Serializable
    @SerialName("no_access")
    object NoAccess : CreateInviteResult
}