package com.y9vad9.pomodoro.backend.providers

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository

interface AccessTokenProvider {
    fun provide(): AuthorizationsRepository.AccessToken
}