package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.MockedSessionsRepository
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest

class StartTimerUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = StartTimerUseCase(repository, MockedCurrentTimeProvider, MockedSessionsRepository())

    @BeforeTest
    fun before() {
        runBlocking {
            repository.createTimer(
                TimerName("test1"),
                TimersRepository.Settings.Default, UsersRepository.UserId(0),
                MockedCurrentTimeProvider.provide()
            )
        }
    }

    @Test
    fun testSuccess() = runBlocking {
        val timerId = TimersRepository.TimerId(0)
        val result = useCase(
            UsersRepository.UserId(0),
            timerId
        )
        assert(result is StartTimerUseCase.Result.Success)
    }

    @Test
    fun testNoAccess() = runBlocking {
        val result = useCase(UsersRepository.UserId(2), TimersRepository.TimerId(0))
        assert(result is StartTimerUseCase.Result.NoAccess)
    }

    @Test
    fun testNotFound() = runBlocking {
        val result = useCase(
            UsersRepository.UserId(2), TimersRepository.TimerId(5)
        )
        assert(result is StartTimerUseCase.Result.NoAccess)
    }
}