package com.y9vad9.pomodoro.backend.application.routes.auth

import com.y9vad9.pomodoro.backend.usecases.auth.AuthViaGoogleUseCase
import com.y9vad9.pomodoro.backend.usecases.auth.GetUserIdByAccessTokenUseCase
import com.y9vad9.pomodoro.backend.usecases.auth.RefreshTokenUseCase
import com.y9vad9.pomodoro.backend.usecases.auth.RemoveAccessTokenUseCase
import io.ktor.server.routing.*

fun Route.authRoot(
    authViaGoogle: AuthViaGoogleUseCase,
    removeToken: RemoveAccessTokenUseCase,
    getUserId: GetUserIdByAccessTokenUseCase,
    refreshToken: RefreshTokenUseCase
) {
    route("auth") {
        authViaGoogle(authViaGoogle)
        removeToken(removeToken)
        getUserId(getUserId)
        renewToken(refreshToken)
    }
}