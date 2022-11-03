package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object JoinByInviteCodeRequest {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val timerId: Int) : Result
        object NotFound : Result
    }
}

fun Route.joinByInviteCode(joinByInviteCode: JoinByInviteUseCase) {
    post("/join") {
        val code: String = call.request.queryParameters.getOrFail("code")
        authorized { userId ->
            val result = joinByInviteCode(
                userId, TimerInvitesRepository.Code(code)
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