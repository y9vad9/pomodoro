package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.value.AccessToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SignWithGoogleResult {
    @SerialName("success")
    @Serializable
    class Success(
        @SerialName("access_token") val accessToken: AccessToken
    ) : SignWithGoogleResult
}