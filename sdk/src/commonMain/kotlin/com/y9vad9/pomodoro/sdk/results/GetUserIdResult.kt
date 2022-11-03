package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.UserId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

public sealed interface GetUserIdResult {
    @Serializable
    @SerialName("success")
    @JvmInline
    public value class Success(public val userId: UserId) : GetUserIdResult
}