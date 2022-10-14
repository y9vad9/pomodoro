package com.y9vad9.pomodoro.backend.repositories.integration

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.TimersDatabaseDataSource
import com.y9vad9.pomodoro.backend.repositories.TimersRepository as TimersRepositoryContract

class TimersRepository(private val datasource: TimersDatabaseDataSource) : TimersRepositoryContract {
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

    override suspend fun getTimers(
        userId: UsersRepository.UserId,
        boundaries: IntProgression
    ): Sequence<TimersRepository.Timer> {
        return datasource.getUserTimers(userId.int, boundaries).map { it.toExternalTimer() }
    }

    override suspend fun createEvent(timerId: TimersRepository.TimerId, timerEvent: TimersRepository.TimerEvent) {
        datasource.addEvent(
            timerId.int,
            if (timerEvent is TimersRepository.TimerEvent.Started)
                TimersDatabaseDataSource.Timer.Event.Type.START
            else TimersDatabaseDataSource.Timer.Event.Type.PAUSE,
            timerEvent.startedAt.long,
            timerEvent.finishesAt?.long
        )
    }

    override suspend fun getEvents(
        timerId: TimersRepository.TimerId,
        boundaries: IntProgression
    ): Sequence<TimersRepository.TimerEvent> {
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

    private fun TimersDatabaseDataSource.Timer.Event.toExternalEvent(): TimersRepositoryContract.TimerEvent {
        return when (eventType) {
            TimersDatabaseDataSource.Timer.Event.Type.PAUSE ->
                TimersRepositoryContract.TimerEvent.Paused(
                    DateTime(startedAt), finishesAt?.let { DateTime(it) }
                )

            TimersDatabaseDataSource.Timer.Event.Type.START ->
                TimersRepositoryContract.TimerEvent.Started(DateTime(startedAt), DateTime(finishesAt!!))
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