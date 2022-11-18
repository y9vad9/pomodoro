package com.y9vad9.pomodoro.backend.usecases.timers

import com.y9vad9.pomodoro.backend.repositories.TimerUpdatesRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GetLiveUpdatesUseCase(
    private val timers: TimersRepository,
    private val timerUpdates: TimerUpdatesRepository,
    private val startTimer: StartTimerUseCase,
    private val stopTimer: StopTimerUseCase
) {
    suspend operator fun invoke(
        userId: UsersRepository.UserId,
        timerId: TimersRepository.TimerId,
        commands: Flow<Command>,
        coroutineScope: CoroutineScope,
    ): Result {
        if (!timers.isMemberOf(userId, timerId))
            return Result.NoAccess

        val updates = MutableSharedFlow<TimerUpdatesRepository.Update>()

        coroutineScope.launch {
            commands.collectLatest {
                when (it) {
                    is Command.Start -> startTimer(userId, timerId)
                    is Command.Stop -> stopTimer(userId, timerId)
                }
            }
        }

        coroutineScope.launch {
            timerUpdates.receiveUpdates(timerId).collectLatest {
                updates.emit(it)
            }
        }

        return Result.Success(
            updates
        )
    }

    sealed interface Command {
        object Start : Command
        object Stop : Command
    }

    sealed interface Result {
        object NoAccess : Result

        @JvmInline
        value class Success(val flow: Flow<TimerUpdatesRepository.Update>) : Result
    }
}