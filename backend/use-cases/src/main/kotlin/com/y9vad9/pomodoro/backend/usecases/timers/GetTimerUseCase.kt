package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.repositories.TimersRepository

class GetTimerUseCase(
    private val timers: TimersRepository
) {
    suspend operator fun invoke(timerId: TimersRepository.TimerId): Result {
        return Result.Success(timers.getTimer(timerId) ?: return Result.NotFound)
    }

    sealed interface Result {
        @JvmInline
        value class Success(val timer: TimersRepository.Timer) : Result
        object NotFound : Result
    }
}