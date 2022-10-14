package com.y9vad9.pomodoro.backend.usecases.timers.invites

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.MockedCodeProvider
import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateInviteUseCaseTest {
    private val invitesRepo = MockedTimerInvitesRepository()
    private val timersRepo = MockedTimersRepository()
    private val useCase = CreateInviteUseCase(
        invitesRepo, timersRepo, MockedCodeProvider
    )

    @Test
    fun testSuccess() {
        runBlocking {
            val id = timersRepo.createTimer(
                TimerName("Test"),
                TimersRepository.Settings.Default,
                UsersRepository.UserId(1),
                MockedCurrentTimeProvider.provide()
            )

            val result = useCase.invoke(
                UsersRepository.UserId(1),
                id,
                TimerInvitesRepository.Limit(2)
            )

            assert(result is CreateInviteUseCase.Result.Success)
        }
    }

    @Test
    fun testNoAccess() {
        runBlocking {
            val id = timersRepo.createTimer(
                TimerName("Test"),
                TimersRepository.Settings.Default,
                UsersRepository.UserId(2),
                MockedCurrentTimeProvider.provide()
            )

            val result = useCase.invoke(
                UsersRepository.UserId(1),
                id,
                TimerInvitesRepository.Limit(1)
            )

            assert(result is CreateInviteUseCase.Result.NoAccess)
        }
    }
}