package com.y9vad9.pomodoro.backend.provider

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository

fun interface RefreshTokenProvider {
    fun provide(): AuthorizationsRepository.RefreshToken
}