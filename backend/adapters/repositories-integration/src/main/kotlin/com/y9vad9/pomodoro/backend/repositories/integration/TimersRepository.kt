package com.y9vad9.pomodoro.backend.repositories.integration

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository.TimerEvent
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.TimersDatabaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import com.y9vad9.pomodoro.backend.repositories.TimersRepository as TimersRepositoryContract

class TimersRepository(
    private val datasource: TimersDatabaseDataSource
) : TimersRepositoryContract {
    class Update(val timerId: TimersRepository.TimerId, val event: TimerEvent)

    private val updates: MutableSharedFlow<Update> = MutableSharedFlow(60)

    override suspend fun createTimer(
        name: TimerName,
        settings: TimersRepository.Settings,
        ownerId: UsersRepository.UserId,
        creationTime: DateTime
    ): TimersRepository.TimerId {
        return TimersRepositoryContract.TimerId(
            datasource.createTimer(
                name.string,
                creationTime.long,
                ownerId.int,
                settings.toInternalSettings()
            )
        )
    }

    override suspend fun getTimer(timerId: TimersRepository.TimerId): TimersRepository.Timer? {
        return datasource.getTimerById(timerId.int)?.toExternalTimer()
    }

    override suspend fun removeTimer(timerId: TimersRepository.TimerId) {
        datasource.removeTimer(timerId.int)
    }

    override suspend fun getTimerSettings(timerId: TimersRepository.TimerId): TimersRepository.Settings? {
        return datasource.getSettings(timerId.int)?.toExternalSettings()
    }

    override suspend fun setTimerSettings(timerId: TimersRepository.TimerId, settings: TimersRepository.NewSettings) {
        datasource.setNewSettings(timerId.int, settings.toInternalPatchable())
    }

    override suspend fun addMember(userId: UsersRepository.UserId, timerId: TimersRepository.TimerId) {
        datasource.addMember(timerId.int, userId.int)
    }

    override suspend fun getMembers(
        timerId: TimersRepository.TimerId,
        boundaries: IntProgression
    ): Sequence<UsersRepository.UserId> {
        return datasource.getMembersIds(timerId.int, boundaries)
            .map { UsersRepository.UserId(it) }
    }

    override suspend fun isMemberOf(userId: UsersRepository.UserId, timerId: TimersRepository.TimerId): Boolean {
        return datasource.isMemberOf(timerId.int, userId.int)
    }

    override fun getEventUpdates(
        timerId: TimersRepository.TimerId,
        lastReceivedId: TimerEvent.TimerEventId
    ): Flow<TimerEvent> {
        return updates.filter {
            it.timerId == timerId && it.event.id.long < lastReceivedId.long
        }.map { it.event }
    }

    override suspend fun getEventsUntil(
        eventId: TimerEvent.TimerEventId,
        boundaries: IntProgression
    ): List<TimerEvent> {
        return datasource.getEventsUntil(eventId.long).map { it.toExternalEvent() }
    }

    override suspend fun getTimers(
        userId: UsersRepository.UserId,
        boundaries: IntProgression
    ): Sequence<TimersRepository.Timer> {
        return datasource.getUserTimers(userId.int, boundaries).map { it.toExternalTimer() }
    }

    override suspend fun createEvent(
        timerId: TimersRepository.TimerId,
        startedAt: DateTime,
        finishesAt: DateTime?,
        isPause: Boolean
    ) {
        datasource.addEvent(
            timerId.int,
            if (!isPause)
                TimersDatabaseDataSource.Timer.Event.Type.START
            else TimersDatabaseDataSource.Timer.Event.Type.PAUSE,
            startedAt.long,
            finishesAt?.long
        )
    }

    override suspend fun getEvents(
        timerId: TimersRepository.TimerId,
        boundaries: IntProgression
    ): Sequence<TimerEvent> {
        return datasource.getEvents(timerId.int, boundaries).map { it.toExternalEvent() }
    }

    private fun TimersDatabaseDataSource.Timer.toExternalTimer(): TimersRepositoryContract.Timer {
        return TimersRepository.Timer(
            TimersRepository.TimerId(id),
            TimerName(timerName),
            UsersRepository.UserId(ownerId),
            settings.toExternalSettings()
        )
    }

    private fun TimersDatabaseDataSource.Timer.Event.toExternalEvent(): TimerEvent {
        return when (eventType) {
            TimersDatabaseDataSource.Timer.Event.Type.PAUSE ->
                TimerEvent.Paused(
                    TimerEvent.TimerEventId(id),
                    DateTime(startedAt),
                    finishesAt?.let { DateTime(it) }
                )

            TimersDatabaseDataSource.Timer.Event.Type.START ->
                TimerEvent.Started(
                    TimerEvent.TimerEventId(id),
                    DateTime(startedAt),
                    DateTime(finishesAt!!)
                )
        }
    }

    private fun TimersDatabaseDataSource.Timer.Settings.toExternalSettings(): TimersRepositoryContract.Settings {
        return TimersRepositoryContract.Settings(
            workTime, restTime, bigRestTime, bigRestEnabled, bigRestPer, isPaused, isEveryoneCanPause
        )
    }

    private fun TimersRepositoryContract.NewSettings.toInternalPatchable(): TimersDatabaseDataSource.Timer.Settings.Patchable {
        return TimersDatabaseDataSource.Timer.Settings.Patchable(
            workTime, restTime, bigRestTime, bigRestEnabled, bigRestPer, isEveryoneCanPause
        )
    }

    private fun TimersRepositoryContract.Settings.toInternalSettings(): TimersDatabaseDataSource.Timer.Settings {
        return TimersDatabaseDataSource.Timer.Settings(
            workTime, restTime, bigRestTime, bigRestEnabled, bigRestPer, isEveryoneCanPause, isPaused
        )
    }
}