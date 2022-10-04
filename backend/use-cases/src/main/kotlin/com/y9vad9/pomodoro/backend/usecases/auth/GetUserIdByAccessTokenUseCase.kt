package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class GetUserIdByAccessTokenUseCase(
    private val authorizations: AuthorizationsRepository
) {
    suspend operator fun invoke(accessToken: AuthorizationsRepository.AccessToken): Result {
        val auth = authorizations.get(accessToken) ?: return Result.NotFound
        return Result.Success(auth.userId)
    }

    sealed interface Result {
        @JvmInline
        value class Success(val userId: UsersRepository.UserId) : Result
        object NotFound : Result
    }
}