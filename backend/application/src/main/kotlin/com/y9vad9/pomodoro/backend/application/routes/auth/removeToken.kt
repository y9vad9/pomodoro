package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.usecases.auth.RemoveAccessTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

object RemoveTokenRequest {
    @Serializable
    sealed interface Result {
        @Serializable
        object Success : Result
    }
}

fun Route.removeToken(removeToken: RemoveAccessTokenUseCase) {
    delete {
        val accessToken = call.request.header(HttpHeaders.Authorization)

        if (accessToken == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@delete
        }

        when (removeToken(AuthorizationsRepository.AccessToken(accessToken))) {
            is RemoveAccessTokenUseCase.Result.AuthorizationNotFound -> {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }

            is RemoveAccessTokenUseCase.Result.Success ->
                call.respond(RemoveTokenRequest.Result.Success)
        }
    }
}