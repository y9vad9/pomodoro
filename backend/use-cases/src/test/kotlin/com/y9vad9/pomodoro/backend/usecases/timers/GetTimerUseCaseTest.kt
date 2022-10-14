package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest

class GetTimerUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = GetTimerUseCase(repository)

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
                TimersRepository.Settings.Default, UsersRepository.UserId(0),
                MockedCurrentTimeProvider.provide()
            )
        }
    }

    @Test
    fun testSuccess() = runBlocking {
        val result = useCase(UsersRepository.UserId(0), TimersRepository.TimerId(1))
        assert(result is GetTimerUseCase.Result.Success)
        result as GetTimerUseCase.Result.Success
        assert(result.timer.name.string == "test2")
    }

    @Test
    fun testNotFound() = runBlocking {
        val result = useCase(UsersRepository.UserId(0), TimersRepository.TimerId(2))
        assert(result is GetTimerUseCase.Result.NotFound)
    }

    @Test
    fun testNoAccess() = runBlocking {
        val result = useCase(UsersRepository.UserId(1), TimersRepository.TimerId(1))
        assert(result is GetTimerUseCase.Result.NotFound)
    }
}