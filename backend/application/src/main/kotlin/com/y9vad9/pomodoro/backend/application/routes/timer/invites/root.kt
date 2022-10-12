package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.usecases.timers.invites.CreateInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.invites.RemoveInviteUseCase
import io.ktor.server.routing.*

fun Route.timerInvites(
    createInviteUseCase: CreateInviteUseCase,
    getInvitesUseCase: GetInvitesUseCase,
    joinByInviteUseCase: JoinByInviteUseCase,
    removeInviteUseCase: RemoveInviteUseCase
) = route("invites") {
    createInvite(createInviteUseCase)
    getInvites(getInvitesUseCase)
    joinByInviteCode(joinByInviteUseCase)
    removeInvite(removeInviteUseCase)
}