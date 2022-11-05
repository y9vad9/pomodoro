package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.Invite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface GetInvitesResult {
    @Serializable
    @SerialName("success")
    public class Success(public val list: List<Invite>) : GetInvitesResult

    @Serializable
    @SerialName("no_access")
    public object NoAccess : GetInvitesResult
}