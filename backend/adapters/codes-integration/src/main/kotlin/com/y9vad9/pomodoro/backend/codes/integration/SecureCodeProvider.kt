package com.y9vad9.pomodoro.backend.codes.integration

import com.y9vad9.pomodoro.backend.providers.CodeProvider
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import java.security.SecureRandom
import kotlin.random.asKotlinRandom

object SecureCodeProvider : CodeProvider {
    private val secureRandom = SecureRandom()
    private val alphabet: List<Char> =
        ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun provide(): TimerInvitesRepository.Code {
        return TimerInvitesRepository.Code(CharArray(8) {
            alphabet.random(secureRandom.asKotlinRandom())
        }.joinToString(""))
    }
}