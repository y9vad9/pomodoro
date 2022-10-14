package com.y9vad9.pomodoro.backend.repositories.integration.datasource

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.repositories.integration.tables.AuthorizationsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class AuthorizationsDataSource(
    private val database: Database
) {

    init {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(AuthorizationsTable)
        }
    }

    suspend fun create(
        userId: UsersRepository.UserId,
        accessToken: AuthorizationsRepository.AccessToken,
        refreshToken: AuthorizationsRepository.RefreshToken,
        expiresAt: DateTime
    ): Unit = newSuspendedTransaction(db = database) {
        AuthorizationsTable.insert(
            AuthorizationsTable.Authorization(
                userId, accessToken, refreshToken, expiresAt
            )
        )
    }

    suspend fun remove(
        accessToken: AuthorizationsRepository.AccessToken
    ): Boolean = newSuspendedTransaction(db = database) {
        AuthorizationsTable.remove(accessToken) > 0
    }

    suspend fun get(
        accessToken: AuthorizationsRepository.AccessToken,
        currentTime: DateTime
    ): AuthorizationsTable.Authorization? = newSuspendedTransaction(db = database) {
        AuthorizationsTable.select(accessToken, currentTime)
    }

    suspend fun getList(
        userId: UsersRepository.UserId
    ): List<AuthorizationsTable.Authorization> = newSuspendedTransaction(db = database) {
        AuthorizationsTable.selectAll(userId)
    }

    suspend fun renew(
        refreshToken: AuthorizationsRepository.RefreshToken,
        accessToken: AuthorizationsRepository.AccessToken,
        expiresAt: DateTime
    ): AuthorizationsTable.Authorization? = newSuspendedTransaction(db = database) {
        AuthorizationsTable.update(refreshToken, accessToken, expiresAt)
        AuthorizationsTable.select(accessToken, DateTime(0))
    }
}