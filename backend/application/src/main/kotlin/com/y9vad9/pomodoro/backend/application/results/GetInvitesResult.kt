package com.y9vad9.pomodoro.backend.application.results

import com.y9vad9.pomodoro.backend.application.types.Invite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GetInvitesResult {
    @Serializable
    @SerialName("success")
    class Success(val list: List<Invite>) : GetInvitesResult

    @Serializable
    @SerialName("no_access")
    object NoAccess : GetInvitesResult
}