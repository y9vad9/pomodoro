package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
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
                TimersRepository.TimerName("test1"),
                TimersRepository.Settings.Default, UserId(0)
            )
            repository.createTimer(
                TimersRepository.TimerName("test2"),
                TimersRepository.Settings.Default, UserId(0)
            )
        }
    }

    @Test
    fun testSuccess() = runBlocking {
        val result = useCase(TimersRepository.TimerId(1))
        assert(result is GetTimerUseCase.Result.Success)
        result as GetTimerUseCase.Result.Success
        assert(result.timer.name.string == "test2")
    }

    @Test
    fun testFailure() = runBlocking {
        val result = useCase(TimersRepository.TimerId(2))
        assert(result is GetTimerUseCase.Result.NotFound)
    }
}