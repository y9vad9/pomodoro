package com.y9vad9.pomodoro.backend.repositories.integration

import com.y9vad9.pomodoro.backend.domain.Milliseconds
import com.y9vad9.pomodoro.backend.repositories.TimerUpdatesRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import com.y9vad9.pomodoro.backend.repositories.TimerUpdatesRepository as Contract

object TimerUpdatesRepository : Contract {
    private val updates =
        MutableSharedFlow<Pair<TimersRepository.TimerId, Contract.Update>>()

    private val schedules = ConcurrentHashMap<TimersRepository.TimerId, Job>()

    override suspend fun sendUpdate(update: Contract.Update, timerId: TimersRepository.TimerId) {
        updates.emit(timerId to update)
    }

    override suspend fun receiveUpdates(
        timerId: TimersRepository.TimerId
    ): Flow<Contract.Update> {
        return updates.filter { (id, _) -> timerId == id }.map { it.second }
    }

    override suspend fun scheduleNext(
        timerId: TimersRepository.TimerId,
        update: TimerUpdatesRepository.Update,
        after: Milliseconds,
        coroutineScope: CoroutineScope
    ) {
        schedules[timerId]?.cancel()
        schedules[timerId] = coroutineScope.launch {
            delay(after.long)
            sendUpdate(update, timerId)
        }
    }
}