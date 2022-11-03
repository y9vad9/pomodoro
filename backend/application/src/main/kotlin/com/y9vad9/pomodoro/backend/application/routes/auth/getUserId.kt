package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

object GetUserIdRequest {
    @Serializable
    sealed interface Result {
        @Serializable
        @JvmInline
        value class Success(val userId: Int) : Result
    }
}

fun Route.getUserId() = get("user-id") {
    authorized {
        call.respond(GetUserIdRequest.Result.Success(it.int))
    }
}