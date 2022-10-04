package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.entity.AccessToken
import com.y9vad9.pomodoro.backend.domain.entity.UserId

class MockedAuthorizationsRepository : AuthorizationsRepository {
    private val authorizations: MutableList<AuthorizationsRepository.Authorization> = mutableListOf()
    override suspend fun create(userId: UserId, accessToken: AccessToken, expiresAt: DateTime) {
        authorizations += AuthorizationsRepository.Authorization(userId, accessToken, expiresAt)
    }

    override suspend fun remove(userId: UserId, accessToken: AccessToken): Boolean {
        return authorizations.removeIf { it.userId == userId && it.accessToken == accessToken }
    }

    override suspend fun get(accessToken: AccessToken): AuthorizationsRepository.Authorization? {
        return authorizations.firstOrNull { it.accessToken == accessToken }
    }

    override suspend fun getList(userId: UserId): List<AuthorizationsRepository.Authorization> {
        return authorizations.filter { it.userId == userId }
    }

}