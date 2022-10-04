package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest

class GetTimersUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = GetTimersUseCase(repository)

    @BeforeTest
    fun before() {
        runBlocking {
            repository.createTimer(
                TimersRepository.TimerName("test1"),
                TimersRepository.Settings.Default, UserId(0)
            )
            repository.createTimer(
                TimersRepository.TimerName("test2"),
                TimersRepository.Settings.Default, UserId(1)
            )
        }
    }

    @Test
    fun testSuccess() = runBlocking {
        val result = useCase(UserId(0))
        assert(result is GetTimersUseCase.Result.Success)
        result as GetTimersUseCase.Result.Success
        assert(result.list.size == 1)
    }
}