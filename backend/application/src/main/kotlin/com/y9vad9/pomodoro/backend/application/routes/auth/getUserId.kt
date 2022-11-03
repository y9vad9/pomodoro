package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.results.GetUserIdResult
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserId() = get("user-id") {
    authorized {
        call.respond<GetUserIdResult>(
            GetUserIdResult.Success(it.serializable())
        )
    }
}