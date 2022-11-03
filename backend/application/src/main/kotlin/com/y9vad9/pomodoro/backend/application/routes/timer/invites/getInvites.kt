package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.Invite
import com.y9vad9.pomodoro.backend.application.types.toExternal
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object GetInvitesRequest {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val list: List<Invite>) : Result
        object NoAccess : Result
    }
}

fun Route.getInvites(getInvites: GetInvitesUseCase) {
    get("/all") {
        val timerId: Int = call.request.queryParameters.getOrFail("timerId").toInt()
        authorized { userId ->
            val result = getInvites(
                userId, TimersRepository.TimerId(
                    timerId
                )
            )

            val response = when (result) {
                is GetInvitesUseCase.Result.Success ->
                    GetInvitesRequest.Result.Success(result.list.map { it.toExternal() })

                is GetInvitesUseCase.Result.NoAccess ->
                    GetInvitesRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}