package com.y9vad9.pomodoro.backend.application.routes.timer.invites

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.GetInvitesResult
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.invites.GetInvitesUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.getInvites(getInvites: GetInvitesUseCase) {
    get("/all") {
        val timerId: Int = call.request.queryParameters.getOrFail("timer_id").toInt()
        authorized { userId ->
            val result = getInvites(
                userId, TimersRepository.TimerId(
                    timerId
                )
            )

            val response = when (result) {
                is GetInvitesUseCase.Result.Success ->
                    GetInvitesResult.Success(result.list.map { it.serializable() })

                is GetInvitesUseCase.Result.NoAccess ->
                    GetInvitesResult.NoAccess
            }

            call.respond(response)
        }
    }
}