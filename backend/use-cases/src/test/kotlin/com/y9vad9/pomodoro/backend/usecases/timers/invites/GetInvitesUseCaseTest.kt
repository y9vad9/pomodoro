package com.y9vad9.pomodoro.backend.usecases.timers.invites

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.provider.MockedCodeProvider
import com.y9vad9.pomodoro.backend.provider.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetInvitesUseCaseTest {
    private val invitesRepo = MockedTimerInvitesRepository()
    private val timersRepo = MockedTimersRepository()
    private val useCase = GetInvitesUseCase(
        invitesRepo, timersRepo
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
            invitesRepo.createInvite(
                id,
                MockedCodeProvider.provide(),
                TimerInvitesRepository.Limit(5)
            )

            val result = useCase.invoke(
                UsersRepository.UserId(1),
                id
            )
            assert(result is GetInvitesUseCase.Result.Success)
            result as GetInvitesUseCase.Result.Success
            assert(result.list.size == 1)
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
                id
            )

            assert(result is GetInvitesUseCase.Result.NoAccess)
        }
    }
}