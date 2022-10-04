package com.y9vad9.pomodoro.backend.repositories

class MockedTimersRepository : TimersRepository {
    private class Timer(
        val events: MutableList<TimersRepository.TimerEvent>,
        var settings: TimersRepository.Settings,
        val ownerId: UsersRepository.UserId,
        val members: MutableList<UsersRepository.UserId>,
        val name: TimersRepository.TimerName
    )

    private val timers: MutableList<Timer> = mutableListOf()

    override suspend fun createTimer(
        name: TimersRepository.TimerName,
        settings: TimersRepository.Settings,
        ownerId: UsersRepository.UserId
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
                isPaused = settings.isPaused ?: it.isPaused,
                isEveryoneCanPause = settings.isEveryoneCanPause ?: it.isEveryoneCanPause
            )
        }
    }

    override suspend fun addMember(userId: UsersRepository.UserId, timerId: TimersRepository.TimerId) {
        timers[timerId.int].members += userId
    }

    override suspend fun getMembers(timerId: TimersRepository.TimerId): List<UsersRepository.UserId> {
        return timers.getOrNull(timerId.int)?.members ?: emptyList()
    }

    override suspend fun isMemberOf(userId: UsersRepository.UserId, timerId: TimersRepository.TimerId): Boolean {
        return timers.getOrNull(timerId.int)?.members?.contains(userId) == true
    }

    override suspend fun getTimers(userId: UsersRepository.UserId): List<TimersRepository.Timer> {
        return timers.filter { it.members.contains(userId) }
            .mapIndexed { i, e -> e.toOriginal(TimersRepository.TimerId(i)) }
    }

    override suspend fun createEvent(
        timerId: TimersRepository.TimerId,
        timerEvent: TimersRepository.TimerEvent
    ) {
        timers[timerId.int].events += timerEvent
    }

    override suspend fun getEvents(timerId: TimersRepository.TimerId): List<TimersRepository.TimerEvent> {
        return timers[timerId.int].events
    }

    private fun Timer.toOriginal(id: TimersRepository.TimerId): TimersRepository. Timer = TimersRepository.Timer(id, name, ownerId, settings)
}