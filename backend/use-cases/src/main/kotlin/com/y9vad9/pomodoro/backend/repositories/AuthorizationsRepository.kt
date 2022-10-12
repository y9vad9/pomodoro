package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime

interface AuthorizationsRepository {
    /**
     * Creates authorization for [userId] with [accessToken] until [expiresAt] time.
     */
    suspend fun create(userId: UsersRepository.UserId, accessToken: AccessToken, expiresAt: DateTime)

    /**
     * Removes authorization for [userId] where [accessToken].
     */
    suspend fun remove(accessToken: AccessToken): Boolean

    /**
     * Gets authorization by [accessToken]
     */
    suspend fun get(accessToken: AccessToken): Authorization?

    /**
     * Gets authorizations by [userId]
     */
    suspend fun getList(userId: UsersRepository.UserId): List<Authorization>

    @JvmInline
    value class AccessToken(val string: String)

    class Authorization(
        val userId: UsersRepository.UserId,
        val accessToken: AccessToken,
        val expiresAt: DateTime
    )
}