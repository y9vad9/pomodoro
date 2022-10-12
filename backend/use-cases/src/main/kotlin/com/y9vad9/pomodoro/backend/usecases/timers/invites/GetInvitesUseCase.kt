package com.y9vad9.pomodoro.backend.usecases.timers.invites

import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class GetInvitesUseCase(
    private val invites: TimerInvitesRepository,
    private val timers: TimersRepository
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        timerId: TimersRepository.TimerId
    ): Result {
        if (timers.getTimer(timerId)?.ownerId != userId)
            return Result.NoAccess

        return Result.Success(invites.getInvites(timerId))
    }

    sealed interface Result {
        @JvmInline
        value class Success(val list: List<TimerInvitesRepository.Invite>) : Result
        object NoAccess : Result
    }
}