package com.y9vad9.pomodoro.backend.providers

import java.security.SecureRandom
import kotlin.random.asKotlinRandom

internal object SecureRandomStringProvider {
    private val alphabet: List<Char> =
        ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val secureRandom: SecureRandom = SecureRandom()

    fun provide(size: Int): String {
        return CharArray(size) {
            alphabet.random(secureRandom.asKotlinRandom())
        }.joinToString("")
    }
}