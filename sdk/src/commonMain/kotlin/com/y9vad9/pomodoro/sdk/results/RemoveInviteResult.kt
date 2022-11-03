package com.y9vad9.pomodoro.sdk.results

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface RemoveInviteResult {
    @SerialName("success")
    public object Success : RemoveInviteResult

    @SerialName("no_access")
    public object NoAccess : RemoveInviteResult

    @SerialName("not_found")
    public object NotFound : RemoveInviteResult
}