package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class RefreshToken(val string: String)

fun AuthorizationsRepository.RefreshToken.serializable() = RefreshToken(string)