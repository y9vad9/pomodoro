package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class RemoveAccessTokenUseCase(
    private val tokens: AuthorizationsRepository
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        accessToken: AuthorizationsRepository.AccessToken
    ): Result {
        return if (tokens.remove(userId, accessToken))
            Result.Success
        else Result.AuthorizationNotFound
    }

    sealed interface Result {
        object Success : Result
        object AuthorizationNotFound : Result
    }
}