package com.y9vad9.pomodoro.backend.provider

import com.y9vad9.pomodoro.backend.domain.DateTime

object MockedCurrentTimeProvider : CurrentTimeProvider {
    override fun provide(): DateTime {
        return DateTime(System.currentTimeMillis())
    }
}