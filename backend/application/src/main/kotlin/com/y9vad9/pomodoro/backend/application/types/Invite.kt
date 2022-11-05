package com.y9vad9.pomodoro.backend.application.types

import com.y9vad9.pomodoro.backend.application.types.value.Code
import com.y9vad9.pomodoro.backend.application.types.value.Count
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Invite(
    @SerialName("places_left") val placesLeft: Count,
    val code: Code
)

fun TimerInvitesRepository.Invite.serializable(): Invite {
    return Invite(limit.serializable(), code.serializable())
}
