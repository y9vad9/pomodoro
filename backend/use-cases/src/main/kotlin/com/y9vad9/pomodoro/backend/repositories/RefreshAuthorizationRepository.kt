package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime

/**
 * Stores refresh tokens for authorizations.
 */
interface RefreshAuthorizationRepository {
    suspend fun create(userId: UsersRepository.UserId, refreshToken: RefreshToken, expiresAt: DateTime)
    suspend fun remove(userId: UsersRepository.UserId, refreshToken: RefreshToken)
    suspend fun get(refreshToken: RefreshToken): RefreshAuthorization
    suspend fun getList(userId: UsersRepository.UserId): List<RefreshAuthorization>

    @JvmInline
    value class RefreshToken(val string: String)

    class RefreshAuthorization(
        val userId: UsersRepository.UserId,
        val refreshToken: RefreshToken
    )
}