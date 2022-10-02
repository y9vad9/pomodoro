package com.y9vad9.pomodoro.backend.domain

@JvmInline
value class DateTime(val long: Long) {
    operator fun plus(long: Long): DateTime = DateTime(this.long + long)
    operator fun plus(dateTime: DateTime): DateTime = DateTime(this.long + dateTime.long)
}