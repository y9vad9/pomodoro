package com.y9vad9.pomodoro.backend.application.types.serializer

import com.y9vad9.pomodoro.backend.application.routes.auth.AuthViaGoogleRequest
import com.y9vad9.pomodoro.backend.application.routes.auth.GetUserIdRequest
import com.y9vad9.pomodoro.backend.application.routes.auth.RemoveTokenRequest
import com.y9vad9.pomodoro.backend.application.routes.auth.RenewTokenRequest
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val jsonModule = SerializersModule {
    polymorphic(AuthViaGoogleRequest.Result::class) {
        subclass(AuthViaGoogleRequest.Result.Success::class)
    }

    polymorphic(GetUserIdRequest.Result::class) {
        subclass(GetUserIdRequest.Result.Success::class)
    }

    polymorphic(RemoveTokenRequest.Result::class) {
        subclass(RemoveTokenRequest.Result.Success::class)
    }

    polymorphic(RenewTokenRequest.Result::class) {
        subclass(RenewTokenRequest.Result.Success::class)
    }
}