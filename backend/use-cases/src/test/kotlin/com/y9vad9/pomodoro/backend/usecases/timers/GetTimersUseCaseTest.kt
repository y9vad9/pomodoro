package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
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
                TimerName("test1"),
                TimersRepository.Settings.Default, UsersRepository.UserId(0),
                MockedCurrentTimeProvider.provide()
            )
            repository.createTimer(
                TimerName("test2"),
                TimersRepository.Settings.Default, UsersRepository.UserId(1),
                MockedCurrentTimeProvider.provide()
            )
        }
    }

    @Test
    fun testSuccess() = runBlocking {
        val result = useCase(UsersRepository.UserId(0), 0..Int.MAX_VALUE)
        assert(result is GetTimersUseCase.Result.Success)
        result as GetTimersUseCase.Result.Success
        assert(result.list.size == 1)
    }
}