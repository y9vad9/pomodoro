package com.y9vad9.pomodoro.client.usecases

import com.y9vad9.pomodoro.sdk.types.value.AccessToken


expect class AuthorizeViaGoogleUseCase {
    operator fun invoke()

    actual sealed interface Result {
        object AuthInvalid : Result
        @JvmInline
        value class Success(val accessToken: AccessToken) : Result
    }
}