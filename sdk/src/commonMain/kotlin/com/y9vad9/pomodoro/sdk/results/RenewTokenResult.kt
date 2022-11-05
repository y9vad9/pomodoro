package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.AccessToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface RenewTokenResult {
    @Serializable
    @SerialName("success")
    public class Success(
        @SerialName("access_token")
        public val accessToken: AccessToken
    ) : RenewTokenResult
}