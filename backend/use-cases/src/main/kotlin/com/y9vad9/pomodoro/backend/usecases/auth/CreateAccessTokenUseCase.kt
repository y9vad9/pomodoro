package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.domain.entity.AccessToken
import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.provider.AccessTokenProvider
import com.y9vad9.pomodoro.backend.provider.CurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class CreateAccessTokenUseCase(
    private val authorizations: AuthorizationsRepository,
    private val users: UsersRepository,
    private val tokenProvider: AccessTokenProvider,
    private val timeProvider: CurrentTimeProvider
) {
    /**
     * Creates token for [userId]
     */
    suspend operator fun invoke(userId: UserId): Result {
        return when {
            users.getUser(userId) == null -> Result.UserNotFound
            else -> {
                val accessToken = tokenProvider.provide()
                authorizations.create(
                    userId,
                    accessToken,
                    timeProvider.provide() + 3600000L
                )
                Result.Success(accessToken)
            }
        }
    }

    sealed interface Result {
        @JvmInline
        value class Success(val accessToken: AccessToken) : Result

        object UserNotFound : Result
    }
}