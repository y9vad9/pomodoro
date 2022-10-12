package com.y9vad9.pomodoro.backend.provider

import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository

interface CodeProvider {
    fun provide(): TimerInvitesRepository.Code
}