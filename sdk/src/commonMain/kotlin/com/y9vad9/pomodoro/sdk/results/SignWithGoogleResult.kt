package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.AccessToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface SignWithGoogleResult {
    @SerialName("success")
    @Serializable
    public class Success(
        @SerialName("access_token")
        public val accessToken: AccessToken
    ) : SignWithGoogleResult
}