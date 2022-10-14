package com.y9vad9.pomodoro.backend.providers

import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository

object MockedCodeProvider : CodeProvider {
    private val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    override fun provide(): TimerInvitesRepository.Code {
        return TimerInvitesRepository.Code(
            CharArray(20) { alphabet.random() }.toString()
        )
    }
}