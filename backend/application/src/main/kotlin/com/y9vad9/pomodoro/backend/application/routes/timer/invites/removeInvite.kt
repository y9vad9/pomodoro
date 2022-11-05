package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.RemoveInviteResult
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.RemoveInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.removeInvite(removeInvite: RemoveInviteUseCase) {
    delete {
        val code: String = call.request.queryParameters.getOrFail("code")
        authorized { userId ->
            val result = removeInvite(
                userId, TimerInvitesRepository.Code(code)
            )

            val response = when (result) {
                is RemoveInviteUseCase.Result.Success ->
                    RemoveInviteResult.Success

                is RemoveInviteUseCase.Result.NoAccess ->
                    RemoveInviteResult.NoAccess

                is RemoveInviteUseCase.Result.NotFound ->
                    RemoveInviteResult.NotFound
            }

            call.respond(response)
        }
    }
}