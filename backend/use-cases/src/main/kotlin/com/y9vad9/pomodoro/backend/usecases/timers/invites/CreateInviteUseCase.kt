package com.y9vad9.pomodoro.backend.usecases.timers.invites

import com.y9vad9.pomodoro.backend.providers.CodeProvider
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class CreateInviteUseCase(
    private val invites: TimerInvitesRepository,
    private val timers: TimersRepository,
    private val codes: CodeProvider
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        timerId: TimersRepository.TimerId,
        limit: TimerInvitesRepository.Limit
    ): Result {
        if (timers.getTimer(timerId)?.ownerId != userId)
            return Result.NoAccess

        val code = codes.provide()
        invites.createInvite(timerId, code, limit)
        return Result.Success(code)
    }

    sealed interface Result {
        @JvmInline
        value class Success(val code: TimerInvitesRepository.Code) : Result
        object NoAccess : Result
    }
}