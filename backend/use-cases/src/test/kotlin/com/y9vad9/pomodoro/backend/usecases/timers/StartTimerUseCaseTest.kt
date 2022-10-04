package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.provider.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest

class StartTimerUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = StartTimerUseCase(repository, MockedCurrentTimeProvider)

    @BeforeTest
    fun before() {
        runBlocking {
            repository.createTimer(
                TimersRepository.TimerName("test1"),
                TimersRepository.Settings.Default, UsersRepository.UserId(0)
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
        assert(!repository.getTimerSettings(timerId)!!.isPaused)
        assert(repository.getEvents(timerId).last() is TimersRepository.TimerEvent.Started)
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