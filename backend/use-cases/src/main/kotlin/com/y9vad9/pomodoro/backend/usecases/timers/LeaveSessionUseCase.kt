package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.repositories.*

class LeaveSessionUseCase(
    private val sessions: SessionsRepository,
    private val schedules: SchedulesRepository
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        timerId: TimersRepository.TimerId
    ): Result {
        sessions.removeMember(timerId, userId)

        if (sessions.count(timerId) == 0) {
            schedules.unbindSingle(timerId)
            schedules.cancel(timerId)
        }

        return Result.Success
    }

    sealed interface Result {
        object Success : Result
    }
}