package com.y9vad9.pomodoro.backend.repositories.integration.tables

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import org.jetbrains.exposed.sql.*

object AuthorizationsTable : Table() {
    val USER_ID = integer("user_id").references(UsersTable.USER_ID)
    val ACCESS_TOKEN = varchar("access_token", 512)
    val REFRESH_TOKEN = varchar("refresh_token", 512)
    val ACCESS_TOKEN_EXPIRES_AT = long("access_token_expires_at")

    fun insert(authorization: Authorization) = insert {
        it[USER_ID] = authorization.userId.int
        it[ACCESS_TOKEN] = authorization.accessToken.string
        it[ACCESS_TOKEN_EXPIRES_AT] = authorization.expiresAt.long
        it[REFRESH_TOKEN] = authorization.refreshToken.string
    }

    fun select(
        accessToken: AuthorizationsRepository.AccessToken,
        currentTime: DateTime
    ): Authorization? =
        select {
            ACCESS_TOKEN eq accessToken.string and
                (ACCESS_TOKEN_EXPIRES_AT greater currentTime.long)
        }.singleOrNull()?.toAuthorization()

    fun update(
        refreshToken: AuthorizationsRepository.RefreshToken,
        accessToken: AuthorizationsRepository.AccessToken,
        expiresAt: DateTime
    ) = update({ REFRESH_TOKEN eq refreshToken.string }) {
        it[ACCESS_TOKEN] = accessToken.string
        it[ACCESS_TOKEN_EXPIRES_AT] = expiresAt.long
    }

    fun selectAll(userId: UsersRepository.UserId): List<Authorization> =
        select { USER_ID eq userId.int }.map { it.toAuthorization() }

    fun remove(accessToken: AuthorizationsRepository.AccessToken) =
        deleteWhere { ACCESS_TOKEN eq accessToken.string }

    class Authorization(
        val userId: UsersRepository.UserId,
        val accessToken: AuthorizationsRepository.AccessToken,
        val refreshToken: AuthorizationsRepository.RefreshToken,
        val expiresAt: DateTime
    )

    private fun ResultRow.toAuthorization() = Authorization(
        UsersRepository.UserId(get(USER_ID)),
        AuthorizationsRepository.AccessToken(get(ACCESS_TOKEN)),
        AuthorizationsRepository.RefreshToken(get(REFRESH_TOKEN)),
        DateTime(get(ACCESS_TOKEN_EXPIRES_AT))
    )
}