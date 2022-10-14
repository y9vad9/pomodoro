package com.y9vad9.pomodoro.backend.providers

import com.y9vad9.pomodoro.backend.domain.DateTime
import java.util.*

class SystemCurrentTimeProvider(private val timeZone: TimeZone) : CurrentTimeProvider {
    override fun provide(): DateTime {
        return DateTime(Calendar.getInstance(timeZone).timeInMillis)
    }
}