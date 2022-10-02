package com.y9vad9.pomodoro.backend.provider

import com.y9vad9.pomodoro.backend.domain.entity.AccessToken

interface AccessTokenProvider {
    fun provide(): AccessToken
}