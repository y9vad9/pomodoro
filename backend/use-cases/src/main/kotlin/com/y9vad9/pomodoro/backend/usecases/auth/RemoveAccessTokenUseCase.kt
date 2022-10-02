package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.domain.entity.AccessToken
import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository

class RemoveAccessTokenUseCase(
    private val tokens: AuthorizationsRepository
) {
    suspend operator fun invoke(userId: UserId, accessToken: AccessToken): Result {
        return if(tokens.remove(userId, accessToken))
            Result.Success
        else Result.AuthorizationNotFound
    }

    sealed interface Result {
        object Success : Result
        object AuthorizationNotFound : Result
    }
}