package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.TimersRepository

class SetTimerSettingsUseCase(
    private val timers: TimersRepository
) {
    suspend operator fun invoke(
        userId: UserId,
        timerId: TimersRepository.TimerId,
        newSettings: TimersRepository.NewSettings
    ): Result {
        if(timers.getTimer(timerId)?.ownerId != userId)
            return Result.NoAccess

        timers.setTimerSettings(
            timerId,
            newSettings
        )

        return Result.Success
    }

    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}