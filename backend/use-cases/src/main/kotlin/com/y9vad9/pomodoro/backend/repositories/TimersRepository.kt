package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.TimerName

interface TimersRepository {
    suspend fun createTimer(
        name: TimerName,
        settings: Settings,
        ownerId: UsersRepository.UserId,
        creationTime: DateTime
    ): TimerId

    suspend fun getTimer(timerId: TimerId): Timer?
    suspend fun removeTimer(timerId: TimerId)

    suspend fun getTimerSettings(timerId: TimerId): Settings?
    suspend fun setTimerSettings(timerId: TimerId, settings: NewSettings)
    suspend fun addMember(userId: UsersRepository.UserId, timerId: TimerId)
    suspend fun getMembers(timerId: TimerId, boundaries: IntProgression): Sequence<UsersRepository.UserId>
    suspend fun isMemberOf(userId: UsersRepository.UserId, timerId: TimerId): Boolean

    /**
     * Gets all timers where [userId] is participating.
     */
    suspend fun getTimers(userId: UsersRepository.UserId, boundaries: IntProgression): Sequence<Timer>

    suspend fun createEvent(timerId: TimerId, timerEvent: TimerEvent)
    suspend fun getEvents(timerId: TimerId, boundaries: IntProgression): Sequence<TimerEvent>

    data class Settings(
        val workTime: Long,
        val restTime: Long,
        val bigRestTime: Long,
        val bigRestEnabled: Boolean,
        val bigRestPer: Int,
        val isPaused: Boolean,
        val isEveryoneCanPause: Boolean
    ) {
        companion object {
            val Default = Settings(
               workTime = 1500000L,
               restTime = 300000,
               bigRestTime = 600000,
               bigRestEnabled = true,
               bigRestPer = 4,
               isPaused = true,
               isEveryoneCanPause = false
           )
       }
   }

    class NewSettings(
        val workTime: Long? = null,
        val restTime: Long? = null,
        val bigRestTime: Long? = null,
        val bigRestEnabled: Boolean? = null,
        val bigRestPer: Int? = null,
        val isEveryoneCanPause: Boolean? = null
    )

    data class Timer(
        val timerId: TimerId,
        val name: TimerName,
        val ownerId: UsersRepository.UserId,
        val settings: Settings
    )

    @JvmInline
    value class TimerId(val int: Int)

    sealed interface TimerEvent {
        val startedAt: DateTime
        val finishesAt: DateTime?

        class Started(
            override val startedAt: DateTime,
            override val finishesAt: DateTime
        ) : TimerEvent
        class Paused(
            override val startedAt: DateTime,
            override val finishesAt: DateTime?
        ) : TimerEvent
    }
}