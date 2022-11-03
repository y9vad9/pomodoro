package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.JoinByCodeResult
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.JoinByInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.joinByInviteCode(joinByInviteCode: JoinByInviteUseCase) {
    post("/join") {
        val code: String = call.request.queryParameters.getOrFail("code")
        authorized { userId ->
            val result = joinByInviteCode(
                userId, TimerInvitesRepository.Code(code)
            )

            val response = when (result) {
                is JoinByInviteUseCase.Result.Success ->
                    JoinByCodeResult.Success(result.timerId.serializable())

                is JoinByInviteUseCase.Result.NotFound ->
                    JoinByCodeResult.NotFound
            }

            call.respond(response)
        }
    }
}