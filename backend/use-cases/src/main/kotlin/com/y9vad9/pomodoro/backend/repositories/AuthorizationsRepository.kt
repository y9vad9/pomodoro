package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.entity.AccessToken
import com.y9vad9.pomodoro.backend.domain.entity.UserId

interface AuthorizationsRepository {
    /**
     * Creates authorization for [userId] with [accessToken] until [expiresAt] time.
     */
    suspend fun create(userId: UserId, accessToken: AccessToken, expiresAt: DateTime)

    /**
     * Removes authorization for [userId] where [accessToken].
     */
    suspend fun remove(userId: UserId, accessToken: AccessToken): Boolean

    /**
     * Gets authorization by [accessToken]
     */
    suspend fun get(accessToken: AccessToken): Authorization

    /**
     * Gets authorizations by [userId]
     */
    suspend fun getList(userId: UserId): List<Authorization>

    class Authorization(
        val userId: UserId,
        val accessToken: AccessToken
    )
}