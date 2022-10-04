package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.domain.entity.AccessToken
import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository

class GetUserIdByAccessTokenUseCase(
    private val authorizations: AuthorizationsRepository
) {
    suspend operator fun invoke(accessToken: AccessToken): Result {
        val auth = authorizations.get(accessToken) ?: return Result.NotFound
        return Result.Success(auth.userId)
    }

    sealed interface Result {
        @JvmInline
        value class Success(val userId: UserId) : Result
        object NotFound : Result
    }
}