package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.RemoveInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object RemoveInviteRequest {
    @Serializable
    sealed interface Result {
        object Success : Result
        object NoAccess : Result
        object NotFound : Result
    }
}

fun Route.removeInvite(removeInvite: RemoveInviteUseCase) {
    delete {
        val code: String = call.request.queryParameters.getOrFail("code")
        authorized { userId ->
            val result = removeInvite(
                userId, TimerInvitesRepository.Code(code)
            )

            val response = when (result) {
                is RemoveInviteUseCase.Result.Success ->
                    RemoveInviteRequest.Result.Success

                is RemoveInviteUseCase.Result.NoAccess ->
                    RemoveInviteRequest.Result.NoAccess

                is RemoveInviteUseCase.Result.NotFound ->
                    RemoveInviteRequest.Result.NotFound
            }

            call.respond(response)
        }
    }
}