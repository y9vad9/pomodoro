package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.Milliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TimerUpdatesRepository {
    suspend fun sendUpdate(
        update: Update,
        timerId: TimersRepository.TimerId
    )

    suspend fun receiveUpdates(timerId: TimersRepository.TimerId): Flow<Update>

    suspend fun scheduleNext(
        timerId: TimersRepository.TimerId,
        update: Update,
        after: Milliseconds,
        coroutineScope: CoroutineScope
    )

    sealed interface Update {
        object Confirmation : Update

        @JvmInline
        value class Settings(
            val newSettings: TimersRepository.NewSettings
        ) : Update

        @JvmInline
        value class TimerStarted(val endsAt: DateTime) : Update

        @JvmInline
        value class TimerStopped(val startsAt: DateTime?) : Update
    }
}