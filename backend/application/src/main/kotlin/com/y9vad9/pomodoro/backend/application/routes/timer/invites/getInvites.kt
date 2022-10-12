package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.Invite
import com.y9vad9.pomodoro.backend.application.types.toExternal
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class GetInvitesRequest(
    val timerId: Int
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val list: List<Invite>) : Result
        object NoAccess : Result
    }
}

fun Route.getInvites(getInvites: GetInvitesUseCase) {
    get("/all") {
        authorized { userId ->
            val data = call.receive<GetInvitesRequest>()
            val result = getInvites(
                userId, TimersRepository.TimerId(
                    data.timerId
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