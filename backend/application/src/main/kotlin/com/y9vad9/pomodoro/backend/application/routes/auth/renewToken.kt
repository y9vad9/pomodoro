package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.usecases.auth.RefreshTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

object RenewTokenRequest {
    @Serializable
    sealed interface Result {
        @Serializable
        @JvmInline
        value class Success(val accessToken: String) : Result
    }
}

fun Route.renewToken(refreshTokenUseCase: RefreshTokenUseCase) {
    post("renew") {
        val refreshToken = call.request.queryParameters.getOrFail("refreshToken")

        val response = when (val result = refreshTokenUseCase(
            AuthorizationsRepository.RefreshToken(refreshToken)
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
