package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.flow.Flow

class GetEventUpdatesUseCase(
    private val timers: TimersRepository
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        timerId: TimersRepository.TimerId,
        lastEventId: TimersRepository.TimerEvent.TimerEventId
    ): Result {
        if (!timers.isMemberOf(userId, timerId))
            return Result.NoAccess

        return Result.Success(
            timers.getEventUpdates(timerId, lastEventId)
        )
    }

    sealed interface Result {
        object NoAccess : Result

        @JvmInline
        value class Success(val flow: Flow<TimersRepository.TimerEvent>) : Result
    }
}