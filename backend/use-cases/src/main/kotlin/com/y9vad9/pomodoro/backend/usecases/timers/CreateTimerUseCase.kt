package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.CurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class CreateTimerUseCase(
    private val timers: TimersRepository,
    private val time: CurrentTimeProvider
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        settings: TimersRepository.Settings,
        name: TimerName
    ): Result {
        return Result.Success(timers.createTimer(name, settings, userId, time.provide()))
    }

    sealed interface Result {
        @JvmInline
        value class Success(val timerId: TimersRepository.TimerId) : Result
    }
}