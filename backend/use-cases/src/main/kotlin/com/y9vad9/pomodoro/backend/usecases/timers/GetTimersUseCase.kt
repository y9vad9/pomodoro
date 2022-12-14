package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class GetTimersUseCase(
    private val timers: TimersRepository
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId, boundaries: IntProgression
    ): Result {
        return Result.Success(timers.getTimers(userId, boundaries).toList())
    }

    sealed interface Result {
        @JvmInline
        value class Success(val list: List<TimersRepository.Timer>) : Result
    }
}