package com.y9vad9.pomodoro.backend.providers

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository

object SecureRefreshTokenProvider : RefreshTokenProvider {
    override fun provide(): AuthorizationsRepository.RefreshToken {
        return AuthorizationsRepository.RefreshToken(
            SecureRandomStringProvider.provide(128)
        )
    }
}