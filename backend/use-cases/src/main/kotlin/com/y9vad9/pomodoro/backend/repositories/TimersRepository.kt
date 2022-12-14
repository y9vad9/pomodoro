package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.Milliseconds
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

    data class Settings(
        val workTime: Milliseconds,
        val restTime: Milliseconds,
        val bigRestTime: Milliseconds,
        val bigRestEnabled: Boolean,
        val bigRestPer: Int,
        val isEveryoneCanPause: Boolean,
        val isConfirmationRequired: Boolean
    ) {
        companion object {
            val Default = Settings(
                workTime = Milliseconds(1500000L),
                restTime = Milliseconds(300000),
                bigRestTime = Milliseconds(600000),
                bigRestEnabled = true,
                bigRestPer = 4,
                isEveryoneCanPause = false,
                isConfirmationRequired = false
            )
        }
    }

    class NewSettings(
        val workTime: Milliseconds? = null,
        val restTime: Milliseconds? = null,
        val bigRestTime: Milliseconds? = null,
        val bigRestEnabled: Boolean? = null,
        val bigRestPer: Int? = null,
        val isEveryoneCanPause: Boolean? = null,
        val isConfirmationRequired: Boolean? = null
    )

    data class Timer(
        val timerId: TimerId,
        val name: TimerName,
        val ownerId: UsersRepository.UserId,
        val settings: Settings
    )

    @JvmInline
    value class TimerId(val int: Int)
}