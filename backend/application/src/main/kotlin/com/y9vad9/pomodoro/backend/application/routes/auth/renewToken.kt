package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.application.results.RenewTokenResult
import com.y9vad9.pomodoro.backend.application.types.value.serializable
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.usecases.auth.RefreshTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.renewToken(refreshTokenUseCase: RefreshTokenUseCase) {
    post("renew") {
        val refreshToken = call.request.queryParameters.getOrFail("refresh_token")

        val result = refreshTokenUseCase(
            AuthorizationsRepository.RefreshToken(refreshToken)
        )
        val response: RenewTokenResult = when (result) {
            is RefreshTokenUseCase.Result.InvalidAuthorization -> {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            is RefreshTokenUseCase.Result.Success ->
                RenewTokenResult.Success(result.accessToken.serializable())
        }
        call.respond(response)
    }
}
