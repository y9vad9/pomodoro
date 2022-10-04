package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.entity.UserId
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class CreateTimerUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = CreateTimerUseCase(repository)

    @Test
    fun testSuccess() {
        runBlocking {
            val result = useCase(
                UserId(0),
                TimersRepository.Settings.Default,
                TimersRepository.TimerName("Test")
            ) as CreateTimerUseCase.Result.Success
            assertNotNull(repository.getTimer(result.timerId))
        }
    }
}