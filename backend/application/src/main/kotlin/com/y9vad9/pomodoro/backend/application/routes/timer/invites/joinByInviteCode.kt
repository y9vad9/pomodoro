package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class JoinByInviteCodeRequest(
    val code: String
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val timerId: Int) : Result
        object NotFound : Result
    }
}

fun Route.joinByInviteCode(joinByInviteCode: JoinByInviteUseCase) {
    post("/join") {
        authorized { userId ->
            val data = call.receive<JoinByInviteCodeRequest>()
            val result = joinByInviteCode(
                userId, TimerInvitesRepository.Code(data.code)
            )

            val response = when (result) {
                is JoinByInviteUseCase.Result.Success ->
                    JoinByInviteCodeRequest.Result.Success(result.timerId.int)

                is JoinByInviteUseCase.Result.NotFound ->
                    JoinByInviteCodeRequest.Result.NotFound
            }

            call.respond(response)
        }
    }
}