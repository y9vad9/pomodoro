package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.usecases.auth.RefreshTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
class RenewTokenRequest(
    val refreshToken: String
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val accessToken: String) : Result
    }
}

fun Route.renewToken(refreshToken: RefreshTokenUseCase) {
    post("google") {
        val data: RenewTokenRequest = call.receive()
        val response = when (val result = refreshToken(
            AuthorizationsRepository.RefreshToken(data.refreshToken)
        )) {
            is RefreshTokenUseCase.Result.InvalidAuthorization -> {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            is RefreshTokenUseCase.Result.Success ->
                AuthViaGoogleRequest.Result.Success(result.accessToken.string)
        }
        call.respond(response)
    }
}