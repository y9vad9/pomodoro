package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.Invite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface GetInvitesResult {
    @SerialName("success")
    @JvmInline
    public value class Success(public val list: List<Invite>) : GetInvitesResult

    @SerialName("no_access")
    public object NoAccess : GetInvitesResult
}