package com.y9vad9.pomodoro.sdk.types.value

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class Count(public val int: Int) {
    init {
        require(int >= 0) { "Count should be equal or be bigger than zero" }
    }
}