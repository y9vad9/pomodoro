package com.y9vad9.pomodoro.backend.application.routes.timer.events

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.GetLastEventsResult
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.events.GetLastEventsUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.getLastEvents(getLastEvents: GetLastEventsUseCase) {
    get("last") {
        authorized { userId ->
            val timerId = call.request.queryParameters.getOrFail("timer_id").toInt()
            val start = call.request.queryParameters.getOrFail("start").toInt()
            val end = call.request.queryParameters.getOrFail("end").toInt()
            val lastKnownId = call.request.queryParameters["last_known_id"]?.toLong()
            val result =
                getLastEvents(
                    userId,
                    start..end,
                    TimersRepository.TimerId(timerId),
                    lastKnownId?.let { id -> TimersRepository.TimerEvent.TimerEventId(id) }
                )

            val response = when (result) {
                is GetLastEventsUseCase.Result.Success -> GetLastEventsResult.Success(
                    result.list.map { it.serializable() }
                )

                is GetLastEventsUseCase.Result.NoAccess -> GetLastEventsResult.NoAccess
            }

            call.respond(response)
        }
    }
}