package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.TimersRepository

class CreateTimerUseCase(
    private val timers: TimersRepository
) {
    suspend operator fun invoke(
        userId: UserId,
        settings: TimersRepository.Settings,
        name: TimersRepository.TimerName
    ): Result {
        return Result.Success(timers.createTimer(name, settings, userId))
    }

    sealed interface Result {
        @JvmInline
        value class Success(val timerId: TimersRepository.TimerId) : Result
    }
}