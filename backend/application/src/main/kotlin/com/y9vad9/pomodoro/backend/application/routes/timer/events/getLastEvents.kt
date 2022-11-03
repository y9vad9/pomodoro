package com.y9vad9.pomodoro.backend.application.routes.timer.events

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.TimerEvent
import com.y9vad9.pomodoro.backend.application.types.toExternal
import com.y9vad9.pomodoro.backend.repositories.TimersRepository
import com.y9vad9.pomodoro.backend.usecases.timers.events.GetLastEventsUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class GetLastEventsRequest(
    val timerId: Int,
    val start: Int,
    val end: Int,
    val lastKnownId: Long?
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val list: List<TimerEvent>) : Result
        object NoAccess : Result
    }
}

fun Route.getLastEvents(getLastEvents: GetLastEventsUseCase) {
    get("last") {
        authorized { userId ->
            val data = call.receive<GetLastEventsRequest>()
            val result =
                getLastEvents(
                    userId,
                    data.start..data.end,
                    TimersRepository.TimerId(data.timerId),
                    data.lastKnownId?.let { id -> TimersRepository.TimerEvent.TimerEventId(id) }
                )

            val response = when (result) {
                is GetLastEventsUseCase.Result.Success -> GetLastEventsRequest.Result.Success(
                    result.list.map { it.toExternal() }
                )

                is GetLastEventsUseCase.Result.NoAccess -> GetLastEventsRequest.Result.NoAccess
            }

            call.respond(response)
        }
    }
}