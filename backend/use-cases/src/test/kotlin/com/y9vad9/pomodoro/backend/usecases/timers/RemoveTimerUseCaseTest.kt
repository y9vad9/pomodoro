package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest

class RemoveTimerUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = RemoveTimerUseCase(repository)

    @BeforeTest
    fun before() {
        runBlocking {
            repository.createTimer(
                TimerName("test1"),
                TimersRepository.Settings.Default, UsersRepository.UserId(0),
                MockedCurrentTimeProvider.provide()
            )
            repository.createTimer(
                TimerName("test2"),
                TimersRepository.Settings.Default, UsersRepository.UserId(2),
                MockedCurrentTimeProvider.provide()
            )
        }
    }

    @Test
    fun testSuccess() = runBlocking {
        val result = useCase(UsersRepository.UserId(2), TimersRepository.TimerId(1))
        assert(result is RemoveTimerUseCase.Result.Success)
    }

    @Test
    fun testNoAccess() = runBlocking {
        val result = useCase(UsersRepository.UserId(2), TimersRepository.TimerId(0))
        assert(result is RemoveTimerUseCase.Result.NotFound)
    }

    @Test
    fun testNotFound() = runBlocking {
        val result = useCase(UsersRepository.UserId(2), TimersRepository.TimerId(5))
        assert(result is RemoveTimerUseCase.Result.NotFound)
    }
}