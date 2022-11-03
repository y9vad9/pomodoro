package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.usecases.auth.AuthViaGoogleUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object AuthViaGoogleRequest {
    @Serializable
    sealed interface Result {
        @Serializable
        @JvmInline
        value class Success(val accessToken: String) : Result
    }
}

fun Route.authViaGoogle(authViaGoogle: AuthViaGoogleUseCase) {
    post("google") {
        val code = call.request.queryParameters.getOrFail("code")
        val response = when (val result = authViaGoogle(code)) {
            is AuthViaGoogleUseCase.Result.InvalidAuthorization -> {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            is AuthViaGoogleUseCase.Result.Success ->
                AuthViaGoogleRequest.Result.Success(result.accessToken.string)
        }
        call.respond(response)
    }
}