package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.routes.timer.invites.timerInvites
import com.y9vad9.pomodoro.backend.usecases.timers.*
import com.y9vad9.pomodoro.backend.usecases.timers.invites.CreateInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.RemoveInviteUseCase
import io.ktor.server.routing.*

fun Route.timersRoot(
    createTimerUseCase: CreateTimerUseCase,
    getTimersUseCase: GetTimersUseCase,
    getTimerUseCase: GetTimerUseCase,
    removeTimerUseCase: RemoveTimerUseCase,
    setTimerSettingsUseCase: SetTimerSettingsUseCase,
    startTimerUseCase: StartTimerUseCase,
    stopTimerUseCase: StopTimerUseCase,
    createInviteUseCase: CreateInviteUseCase,
    getInvitesUseCase: GetInvitesUseCase,
    joinByInviteUseCase: JoinByInviteUseCase,
    removeInviteUseCase: RemoveInviteUseCase,
    joinSessionUseCase: JoinSessionUseCase,
    leaveSessionUseCase: LeaveSessionUseCase,
    confirmStartUseCase: ConfirmStartUseCase
) = route("timers") {
    createTimer(createTimerUseCase)
    getTimers(getTimersUseCase)
    getTimer(getTimerUseCase)
    removeTimer(removeTimerUseCase)
    setSettings(setTimerSettingsUseCase)

    timerInvites(
        createInviteUseCase, getInvitesUseCase, joinByInviteUseCase, removeInviteUseCase
    )

    timerUpdates(
        joinSessionUseCase,
        leaveSessionUseCase,
        confirmStartUseCase,
        startTimerUseCase,
        stopTimerUseCase
    )
}