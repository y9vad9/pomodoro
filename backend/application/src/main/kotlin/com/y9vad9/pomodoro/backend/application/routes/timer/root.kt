package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.routes.timer.events.eventsRoot
import com.y9vad9.pomodoro.backend.application.routes.timer.invites.timerInvites
import com.y9vad9.pomodoro.backend.usecases.timers.*
import com.y9vad9.pomodoro.backend.usecases.timers.events.GetLastEventsUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.CreateInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.RemoveInviteUseCase
import io.ktor.server.routing.*

fun Route.timersRoot(
    createTimer: CreateTimerUseCase,
    getTimers: GetTimersUseCase,
    getTimer: GetTimerUseCase,
    removeTimer: RemoveTimerUseCase,
    setSettings: SetTimerSettingsUseCase,
    startTimer: StartTimerUseCase,
    stopTimer: StopTimerUseCase,
    createInviteUseCase: CreateInviteUseCase,
    getInvitesUseCase: GetInvitesUseCase,
    joinByInviteUseCase: JoinByInviteUseCase,
    removeInviteUseCase: RemoveInviteUseCase,
    getLastEventsUseCase: GetLastEventsUseCase,
    getEventUpdatesUseCase: GetEventUpdatesUseCase
) {
    route("timers") {
        createTimer(createTimer)
        getTimers(getTimers)
        getTimer(getTimer)
        removeTimer(removeTimer)
        setSettings(setSettings)
        startTimer(startTimer)
        stopTimer(stopTimer)

        timerInvites(
            createInviteUseCase,
            getInvitesUseCase,
            joinByInviteUseCase,
            removeInviteUseCase
        )

        eventsRoot(getLastEventsUseCase, getEventUpdatesUseCase)
    }
}