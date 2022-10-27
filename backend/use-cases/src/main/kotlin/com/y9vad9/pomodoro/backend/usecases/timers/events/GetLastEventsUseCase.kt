package com.y9vad9.pomodoro.backend.usecases.timers.events

import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class GetLastEventsUseCase(
    private val timers: TimersRepository
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        boundaries: IntProgression,
        timerId: TimersRepository.TimerId,
        lastKnown: TimersRepository.TimerEvent.TimerEventId?
    ): Result {
        val timer = timers.getTimer(timerId) ?: return Result.NoAccess
        return if (
            (timer.ownerId == userId) || timers.isMemberOf(userId, timerId)
        ) {

            Result.Success(
                if (lastKnown != null)
                    timers.getEventsUntil(lastKnown, boundaries)
                else timers.getEvents(timerId, boundaries).toList()
            )
        } else {
            Result.NoAccess
        }
    }

    sealed interface Result {
        @JvmInline
        value class Success(val list: List<TimersRepository.TimerEvent>) : Result
        object NoAccess : Result
    }
}