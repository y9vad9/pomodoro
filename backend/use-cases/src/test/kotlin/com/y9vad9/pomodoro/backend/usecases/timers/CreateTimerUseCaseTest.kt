package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class CreateTimerUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = CreateTimerUseCase(repository, MockedCurrentTimeProvider)

    @Test
    fun testSuccess() {
        runBlocking {
            val result = useCase(
                UsersRepository.UserId(0),
                TimersRepository.Settings.Default,
                TimerName("Test")
            ) as CreateTimerUseCase.Result.Success
            assertNotNull(repository.getTimer(result.timerId))
        }
    }
}