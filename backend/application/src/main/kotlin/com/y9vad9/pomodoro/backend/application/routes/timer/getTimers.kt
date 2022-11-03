package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.GetTimersResult
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.usecases.timers.GetTimersUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class GetTimersRequest(
    val count: Int,
    val offset: Int
)

fun Route.getTimers(getTimers: GetTimersUseCase) {
    get("all") {
        authorized { userId ->
            val data = call.receive<GetTimersRequest>()
            val result: GetTimersResult =
                GetTimersResult.Success(
                    (getTimers(
                        userId,
                        data.offset..data.count + data.offset
                    ) as GetTimersUseCase.Result.Success).list.map { it.serializable() }
                )

            call.respond(result)
        }
    }
}