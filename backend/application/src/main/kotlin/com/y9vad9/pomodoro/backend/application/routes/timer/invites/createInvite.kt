package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.CreateInviteResult
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.CreateInviteUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.createInvite(createInvite: CreateInviteUseCase) {
    post {
        val timerId: Int = call.request.queryParameters
            .getOrFail("timer_id").toInt()
        val maxJoiners: Int = call.request.queryParameters
            .getOrFail("max_joiners").toInt()

        authorized { userId ->
            val result = createInvite(
                userId, TimersRepository.TimerId(
                    timerId
                ), TimerInvitesRepository.Count(maxJoiners)
            )

            val response: CreateInviteResult = when (result) {
                is CreateInviteUseCase.Result.Success ->
                    CreateInviteResult.Success(result.code.serializable())

                is CreateInviteUseCase.Result.NoAccess ->
                    CreateInviteResult.NoAccess
            }

            call.respond(response)
        }
    }
}