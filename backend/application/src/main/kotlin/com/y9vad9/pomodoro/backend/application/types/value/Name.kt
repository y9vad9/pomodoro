package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.domain.UserName
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Name(val string: String) {
    init {
        require(string.length <= 50) {
            "Name length should not more than 50, but got ${string.length}"
        }
    }
}

fun UserName.serializable() = Name(string)
fun TimerName.serializable() = Name(string)