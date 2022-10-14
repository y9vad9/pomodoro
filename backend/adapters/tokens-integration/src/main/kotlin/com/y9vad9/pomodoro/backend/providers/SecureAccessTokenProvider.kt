package com.y9vad9.pomodoro.backend.providers

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository

object SecureAccessTokenProvider : AccessTokenProvider {
    override fun provide(): AuthorizationsRepository.AccessToken {
        return AuthorizationsRepository.AccessToken(
            SecureRandomStringProvider.provide(128)
        )
    }
}