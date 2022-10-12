package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import kotlinx.serialization.Serializable

@Serializable
class Invite(
    val placesLeft: Int,
    val code: String
)

fun TimerInvitesRepository.Invite.toExternal(): Invite {
    return Invite(limit.int, code.string)
}