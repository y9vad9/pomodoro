package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.domain.Milliseconds
import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.providers.MockedCurrentTimeProvider
import com.y9vad9.pomodoro.backend.repositories.MockedSessionsRepository
import com.y9vad9.pomodoro.backend.repositories.MockedTimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.BeforeTest

class SetTimerSettingsUseCaseTest {
    private val repository = MockedTimersRepository()
    private val useCase = SetTimerSettingsUseCase(repository, MockedSessionsRepository())

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
        val result = useCase(
            UsersRepository.UserId(2),
            TimersRepository.TimerId(1),
            TimersRepository.NewSettings(workTime = Milliseconds(0))
        )
        assert(result is SetTimerSettingsUseCase.Result.Success)
        assert(repository.getTimerSettings(TimersRepository.TimerId(1))!!.workTime == Milliseconds(0L))
    }

    @Test
    fun testNoAccess() = runBlocking {
        val result = useCase(UsersRepository.UserId(2), TimersRepository.TimerId(0), TimersRepository.NewSettings())
        assert(result is SetTimerSettingsUseCase.Result.NoAccess)
    }

    @Test
    fun testNotFound() = runBlocking {
        val result = useCase(
            UsersRepository.UserId(2), TimersRepository.TimerId(5), TimersRepository.NewSettings()
        )
        assert(result is SetTimerSettingsUseCase.Result.NoAccess)
    }
}