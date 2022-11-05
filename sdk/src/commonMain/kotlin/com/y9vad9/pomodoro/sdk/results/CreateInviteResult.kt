package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.Code
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface CreateInviteResult {
    @Serializable
    @SerialName("success")
    public class Success(public val code: Code) : CreateInviteResult

    @Serializable
    @SerialName("no_access")
    public object NoAccess : CreateInviteResult
}