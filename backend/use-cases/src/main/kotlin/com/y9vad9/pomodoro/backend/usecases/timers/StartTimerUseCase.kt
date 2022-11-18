package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.providers.CurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.TimerUpdatesRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class StartTimerUseCase(
    private val timers: TimersRepository,
    private val time: CurrentTimeProvider,
    private val timerUpdates: TimerUpdatesRepository
) {
    suspend operator fun invoke(userId: UsersRepository.UserId, timerId: TimersRepository.TimerId): Result {
        val timer = timers.getTimer(timerId) ?: return Result.NoAccess
        val settings = timers.getTimerSettings(timerId)!!
        return if (
            (timer.ownerId == userId)
            || (settings.isEveryoneCanPause && timers.isMemberOf(userId, timerId))
        ) {
            timerUpdates.sendUpdate(
                TimerUpdatesRepository.Update.TimerStarted(
                    time.provide() + settings.workTime
                ),
                timerId
            )

            Result.Success
        } else {
            Result.NoAccess
        }
    }

    sealed interface Result {
        object Success : Result
        object NoAccess : Result
    }
}