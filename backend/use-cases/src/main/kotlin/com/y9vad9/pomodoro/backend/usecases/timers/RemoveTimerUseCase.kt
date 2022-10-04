package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.TimersRepository

class RemoveTimerUseCase(
    private val timers: TimersRepository
) {
    suspend operator fun invoke(userId: UserId, timerId: TimersRepository.TimerId): Result {
        val timer = timers.getTimer(timerId)
        return when {
            timer == null -> Result.NotFound
            timer.ownerId != userId -> Result.NoAccess
            else -> {
                timers.removeTimer(timerId)
                Result.Success
            }
        }

    }

    sealed interface Result {
        object Success : Result
        object NotFound : Result
        object NoAccess : Result
    }
}