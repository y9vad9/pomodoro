package com.y9vad9.pomodoro.backend.usecases.timers.invites

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.provider.MockedCodeProvider
import com.y9vad9.pomodoro.backend.provider.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class JoinByInviteUseCaseTest {
    private val invitesRepo = MockedTimerInvitesRepository()
    private val timersRepo = MockedTimersRepository()
    private val useCase = JoinByInviteUseCase(
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

            val code = MockedCodeProvider.provide()
            invitesRepo.createInvite(id, code, TimerInvitesRepository.Limit(5))

            val result = useCase.invoke(
                UsersRepository.UserId(1),
                code
            )
            assert(result is JoinByInviteUseCase.Result.Success)
        }
    }

    @Test
    fun testNotFound() {
        runBlocking {
            val id = timersRepo.createTimer(
                TimerName("Test"),
                TimersRepository.Settings.Default,
                UsersRepository.UserId(2),
                MockedCurrentTimeProvider.provide()
            )

            val result = useCase.invoke(
                UsersRepository.UserId(1),
                TimerInvitesRepository.Code("ANY")
            )

            assert(result is JoinByInviteUseCase.Result.NotFound)
        }
    }

    @Test
    fun testInviteDelete() {
        runBlocking {
            val id = timersRepo.createTimer(
                TimerName("Test"),
                TimersRepository.Settings.Default,
                UsersRepository.UserId(1),
                MockedCurrentTimeProvider.provide()
            )

            val code = MockedCodeProvider.provide()
            invitesRepo.createInvite(id, code, TimerInvitesRepository.Limit(1))

            val result = useCase.invoke(
                UsersRepository.UserId(1),
                code
            )

            assert(result is JoinByInviteUseCase.Result.Success)
            result as JoinByInviteUseCase.Result.Success
            assert(invitesRepo.getInvite(code) == null)
        }
    }
}