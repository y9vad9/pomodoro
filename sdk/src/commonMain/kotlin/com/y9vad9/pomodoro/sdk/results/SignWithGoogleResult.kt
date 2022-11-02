package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.AccessToken
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface SignWithGoogleResult {
    @JvmInline
    public value class Success(public val accessToken: AccessToken) : SignWithGoogleResult
}