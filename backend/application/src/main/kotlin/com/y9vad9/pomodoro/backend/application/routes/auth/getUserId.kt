package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.usecases.auth.GetUserIdByAccessTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
class GetUserIdRequest(
    val accessToken: String
) {
    @Serializable
    sealed interface Result {
        @JvmInline
        value class Success(val userId: Int) : Result
    }
}

fun Route.getUserId(getUserId: GetUserIdByAccessTokenUseCase) {
    delete("google") {
        val data: GetUserIdRequest = call.receive()
        val result = getUserId(AuthorizationsRepository.AccessToken(data.accessToken))
        when (result) {
            is GetUserIdByAccessTokenUseCase.Result.NotFound -> {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }

            is GetUserIdByAccessTokenUseCase.Result.Success ->
                call.respond(RemoveTokenRequest.Result.Success)
        }
    }
}