package com.y9vad9.pomodoro.backend.repositories.integration

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.AuthorizationsDataSource
import com.y9vad9.pomodoro.backend.repositories.integration.tables.AuthorizationsTable
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository as Contract

class AuthorizationsRepository(
    private val datasource: AuthorizationsDataSource
) : Contract {

    override suspend fun create(
        userId: UsersRepository.UserId,
        accessToken: AuthorizationsRepository.AccessToken,
        refreshToken: AuthorizationsRepository.RefreshToken,
        expiresAt: DateTime
    ) {
        datasource.create(userId, accessToken, refreshToken, expiresAt)
    }

    override suspend fun remove(accessToken: AuthorizationsRepository.AccessToken): Boolean {
        return datasource.remove(accessToken)
    }

    override suspend fun get(
        accessToken: AuthorizationsRepository.AccessToken,
        currentTime: DateTime
    ): AuthorizationsRepository.Authorization? {
        return datasource.get(accessToken, currentTime)?.toExternal()
    }

    override suspend fun getList(userId: UsersRepository.UserId): List<AuthorizationsRepository.Authorization> {
        return datasource.getList(userId).map { it.toExternal() }
    }

    override suspend fun renew(
        refreshToken: AuthorizationsRepository.RefreshToken,
        accessToken: AuthorizationsRepository.AccessToken,
        expiresAt: DateTime
    ): AuthorizationsRepository.Authorization? {
        return datasource.renew(refreshToken, accessToken, expiresAt)?.toExternal()
    }

    private fun AuthorizationsTable.Authorization.toExternal(): AuthorizationsRepository.Authorization {
        return AuthorizationsRepository.Authorization(
            userId, accessToken, refreshToken, expiresAt
        )
    }
}