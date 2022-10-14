package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.providers.AccessTokenProvider
import com.y9vad9.pomodoro.backend.providers.CurrentTimeProvider
import com.y9vad9.pomodoro.backend.providers.RefreshTokenProvider
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class CreateAccessTokenUseCase(
    private val authorizations: AuthorizationsRepository,
    private val users: UsersRepository,
    private val tokenProvider: AccessTokenProvider,
    private val refreshTokenProvider: RefreshTokenProvider,
    private val timeProvider: CurrentTimeProvider
) {
    /**
     * Creates token for [userId]
     */
    suspend operator fun invoke(userId: UsersRepository.UserId): Result {
        return when {
            users.getUser(userId) == null -> Result.UserNotFound
            else -> {
                val accessToken = tokenProvider.provide()
                authorizations.create(
                    userId,
                    accessToken,
                    refreshTokenProvider.provide(),
                    timeProvider.provide() + 3600000L
                )
                Result.Success(accessToken)
            }
        }
    }

    sealed interface Result {
        @JvmInline
        value class Success(val accessToken: AuthorizationsRepository.AccessToken) : Result

        object UserNotFound : Result
    }
}