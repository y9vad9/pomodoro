package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface RenewTokenResult {
    @SerialName("success")
    @JvmInline
    public value class Success(public val accessToken: String) : RenewTokenResult
}