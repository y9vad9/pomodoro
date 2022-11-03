package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Count(val int: Int) {
    init {
        require(int >= 0) { "Count should be equal or be bigger than zero" }
    }
}

fun TimerInvitesRepository.Count.serializable(): Count = Count(int)