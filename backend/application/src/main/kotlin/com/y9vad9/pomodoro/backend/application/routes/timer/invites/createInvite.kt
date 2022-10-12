package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.CreateInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateInviteRequest(
    val timerId: Int,
    val maxJoiners: Int
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val code: String) : Result
        object NoAccess : Result
    }
}

fun Route.createInvite(createInvite: CreateInviteUseCase) {
    post<CreateInviteRequest> { data ->
        authorized { userId ->
            val result = createInvite(
                userId, TimersRepository.TimerId(
                    data.timerId
                ), TimerInvitesRepository.Limit(data.maxJoiners)
            )

            val response = when (result) {
                is CreateInviteUseCase.Result.Success ->
                    CreateInviteRequest.Result.Success(result.code.string)

                is CreateInviteUseCase.Result.NoAccess ->
                    CreateInviteRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}