package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.usecases.auth.AuthViaGoogleUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
class AuthViaGoogleRequest(
    val code: String
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val accessToken: String) : Result
    }
}

fun Route.authViaGoogle(authViaGoogle: AuthViaGoogleUseCase) {
    post("google") {
        val data: AuthViaGoogleRequest = call.receive()
        val response = when (val result = authViaGoogle(data.code)) {
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