package com.y9vad9.pomodoro.backend.providers

import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository

interface CodeProvider {
    fun provide(): TimerInvitesRepository.Code
}