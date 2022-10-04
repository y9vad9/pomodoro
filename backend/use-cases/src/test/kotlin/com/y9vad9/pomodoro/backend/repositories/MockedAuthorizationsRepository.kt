package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime

class MockedAuthorizationsRepository : AuthorizationsRepository {
    private val authorizations: MutableList<AuthorizationsRepository.Authorization> = mutableListOf()
    override suspend fun create(
        userId: UsersRepository.UserId,
        accessToken: AuthorizationsRepository.AccessToken,
        expiresAt: DateTime
    ) {
        authorizations += AuthorizationsRepository.Authorization(userId, accessToken, expiresAt)
    }

    override suspend fun remove(
        userId: UsersRepository.UserId,
        accessToken: AuthorizationsRepository.AccessToken
    ): Boolean {
        return authorizations.removeIf { it.userId == userId && it.accessToken == accessToken }
    }

    override suspend fun get(accessToken: AuthorizationsRepository.AccessToken): AuthorizationsRepository.Authorization? {
        return authorizations.firstOrNull { it.accessToken == accessToken }
    }

    override suspend fun getList(userId: UsersRepository.UserId): List<AuthorizationsRepository.Authorization> {
        return authorizations.filter { it.userId == userId }
    }

}