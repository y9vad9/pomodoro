package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Code(val string: String)

fun TimerInvitesRepository.Code.serializable() = Code(string)