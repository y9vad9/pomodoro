package com.y9vad9.pomodoro.backend.provider

import com.y9vad9.pomodoro.backend.domain.DateTime

interface CurrentTimeProvider {
    fun provide(): DateTime
}