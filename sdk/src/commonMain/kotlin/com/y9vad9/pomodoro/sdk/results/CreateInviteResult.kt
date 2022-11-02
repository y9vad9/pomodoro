package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.Code
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface CreateInviteResult {
    @SerialName("success")
    @JvmInline
    public value class Success(public val code: Code) : CreateInviteResult

    @SerialName("no_access")
    public object NoAccess : CreateInviteResult
}