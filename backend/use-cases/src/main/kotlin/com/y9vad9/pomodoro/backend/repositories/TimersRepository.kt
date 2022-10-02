package com.y9vad9.pomodoro.backend.repositories

interface TimersRepository {
    suspend fun createTimer(
        teamId: TeamsRepository.TeamId,
        settings: Settings
    ): TimerId

    suspend fun getTimerSettings(timerId: TimerId): Settings?
    suspend fun setTimerSettings(timerId: TimerId, settings: Settings)

    suspend fun createEvent(timerId: TimerId, timerEvent: TimerEvent)
    suspend fun getEvents(timerId: TimerId): List<TimerEvent>

    class Settings(
        val workTime: Long,
        val restTime: Long,
        val bigRestTime: Long,
        val bigRestEnabled: Boolean,
        val bigRestPer: Int,
        val isPaused: Boolean,
        val isEveryoneCanPause: Boolean
    )

    @JvmInline
    value class TimerId(val int: Int)

    sealed interface TimerEvent {
        val startedAt: Long

        class Started(override val startedAt: Long, val finishesAt: Long) : TimerEvent
        class Paused(override val startedAt: Long, val finishesAt: Long?) : TimerEvent
    }
}