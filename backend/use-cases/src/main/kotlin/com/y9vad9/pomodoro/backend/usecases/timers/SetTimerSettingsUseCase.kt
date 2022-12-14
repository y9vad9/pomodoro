package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.repositories.SessionsRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class SetTimerSettingsUseCase(
    private val timers: TimersRepository,
    private val sessions: SessionsRepository
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        timerId: TimersRepository.TimerId,
        newSettings: TimersRepository.NewSettings
    ): Result {
        if (timers.getTimer(timerId)?.ownerId != userId)
            return Result.NoAccess

        timers.setTimerSettings(
            timerId,
            newSettings
        )

        sessions.sendUpdate(timerId, SessionsRepository.Update.Settings(newSettings))

        return Result.Success
    }

    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}