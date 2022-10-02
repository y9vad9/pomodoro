package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.entity.UserId

/**
 * Stores refresh tokens for authorizations.
 */
interface RefreshAuthorizationRepository {
    suspend fun create(userId: UserId, refreshToken: RefreshToken, expiresAt: DateTime)
    suspend fun remove(userId: UserId, refreshToken: RefreshToken)
    suspend fun get(refreshToken: RefreshToken): RefreshAuthorization
    suspend fun getList(userId: UserId): List<RefreshAuthorization>

    @JvmInline
    value class RefreshToken(val string: String)

    class RefreshAuthorization(
        val userId: UserId,
        val refreshToken: RefreshToken
    )
}