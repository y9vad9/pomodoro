package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.UserId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface GetUserIdResult {
    @Serializable
    @SerialName("success")
    public class Success(
        @SerialName("user_id") public val userId: UserId
    ) : GetUserIdResult
}