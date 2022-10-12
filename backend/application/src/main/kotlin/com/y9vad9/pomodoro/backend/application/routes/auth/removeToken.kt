package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.usecases.auth.RemoveAccessTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
class RemoveTokenRequest(
    val accessToken: String
) {
    @Serializable
    sealed interface Result {
        object Success : Result
    }
}

fun Route.removeToken(removeToken: RemoveAccessTokenUseCase) {
    delete("google") {
        val data: RemoveTokenRequest = call.receive()
        val result = removeToken(AuthorizationsRepository.AccessToken(data.accessToken))
        when (result) {
            is RemoveAccessTokenUseCase.Result.AuthorizationNotFound -> {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }

            is RemoveAccessTokenUseCase.Result.Success ->
                call.respond(RemoveTokenRequest.Result.Success)
        }
    }
}