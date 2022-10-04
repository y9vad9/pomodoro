package com.y9vad9.pomodoro.backend.provider

import com.y9vad9.pomodoro.backend.domain.DateTime

fun interface CurrentTimeProvider {
    fun provide(): DateTime
}