package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.TimerName
import kotlinx.coroutines.flow.Flow

class MockedTimersRepository : TimersRepository {
    private data class Timer(
        val events: MutableList<TimersRepository.TimerEvent>,
        var settings: TimersRepository.Settings,
        val ownerId: UsersRepository.UserId,
        val members: MutableList<UsersRepository.UserId>,
        val name: TimerName
    )

    private val timers: MutableList<Timer> = mutableListOf()

    override suspend fun createTimer(
        name: TimerName,
        settings: TimersRepository.Settings,
        ownerId: UsersRepository.UserId,
        creationTime: DateTime
    ): TimersRepository.TimerId {
        timers.add(
            Timer(
                mutableListOf(),
                TimersRepository.Settings.Default,
                ownerId,
                mutableListOf(ownerId),
                name
            )
        )
        return TimersRepository.TimerId(timers.lastIndex)
    }

    override suspend fun getTimer(timerId: TimersRepository.TimerId): TimersRepository.Timer? {
        return timers.getOrNull(timerId.int)?.let {
            TimersRepository.Timer(timerId, it.name, it.ownerId, it.settings)
        }
    }

    override suspend fun removeTimer(timerId: TimersRepository.TimerId) {
        timers.removeAt(timerId.int)
    }

    override suspend fun getTimerSettings(timerId: TimersRepository.TimerId): TimersRepository.Settings? {
        return timers.getOrNull(timerId.int)?.settings
    }

    override suspend fun setTimerSettings(timerId: TimersRepository.TimerId, settings: TimersRepository.NewSettings) {
        timers[timerId.int].settings = timers[timerId.int].settings.let {
            TimersRepository.Settings(
                workTime = settings.workTime ?: it.workTime,
                restTime = settings.restTime ?: it.restTime,
                bigRestTime = settings.bigRestTime ?: it.bigRestTime,
                bigRestEnabled = settings.bigRestEnabled ?: it.bigRestEnabled,
                bigRestPer = settings.bigRestPer ?: it.bigRestPer,
                isPaused = it.isPaused,
                isEveryoneCanPause = settings.isEveryoneCanPause ?: it.isEveryoneCanPause
            )
        }
    }

    override suspend fun addMember(userId: UsersRepository.UserId, timerId: TimersRepository.TimerId) {
        timers[timerId.int].members += userId
    }

    override suspend fun getMembers(
        timerId: TimersRepository.TimerId,
        boundaries: IntProgression
    ): Sequence<UsersRepository.UserId> {
        return (timers.getOrNull(timerId.int)?.members ?: emptyList()).asSequence()
    }

    override suspend fun isMemberOf(userId: UsersRepository.UserId, timerId: TimersRepository.TimerId): Boolean {
        return timers.getOrNull(timerId.int)?.members?.contains(userId) == true
    }

    override fun getEventUpdates(
        timerId: TimersRepository.TimerId,
        lastReceivedId: TimersRepository.TimerEvent.TimerEventId
    ): Flow<TimersRepository.TimerEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventsUntil(
        eventId: TimersRepository.TimerEvent.TimerEventId,
        boundaries: IntProgression
    ): List<TimersRepository.TimerEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun getTimers(
        userId: UsersRepository.UserId,
        boundaries: IntProgression
    ): Sequence<TimersRepository.Timer> {
        return timers.asSequence().filter { it.members.contains(userId) }
            .mapIndexed { i, e -> e.toOriginal(TimersRepository.TimerId(i)) }
    }

    override suspend fun createEvent(
        timerId: TimersRepository.TimerId,
        startedAt: DateTime,
        finishesAt: DateTime?,
        isPause: Boolean
    ) {
        timers[timerId.int].events += if (isPause)
            TimersRepository.TimerEvent.Paused(
                TimersRepository.TimerEvent.TimerEventId(
                    (timers[timerId.int].events.lastIndex + 1).toLong()
                ), startedAt, finishesAt
            )
        else TimersRepository.TimerEvent.Started(
            TimersRepository.TimerEvent.TimerEventId(
                (timers[timerId.int].events.lastIndex + 1).toLong()
            ), startedAt, finishesAt!!
        )
        timers[timerId.int] = timers[timerId.int].let {
            it.copy(
                settings = it.settings.copy(isPaused = isPause)
            )
        }
    }

    override suspend fun getEvents(
        timerId: TimersRepository.TimerId,
        boundaries: IntProgression
    ): Sequence<TimersRepository.TimerEvent> {
        return timers[timerId.int].events.asSequence()
    }

    private fun Timer.toOriginal(id: TimersRepository.TimerId): TimersRepository.Timer =
        TimersRepository.Timer(id, name, ownerId, settings)
}