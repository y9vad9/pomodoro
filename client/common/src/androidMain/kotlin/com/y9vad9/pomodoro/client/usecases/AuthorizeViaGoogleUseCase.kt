package com.y9vad9.pomodoro.client.usecases

import android.app.Application.ActivityLifecycleCallbacks
import androidx.lifecycle.Lifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.internal.ActivityLifecycleObserver
import com.y9vad9.pomodoro.sdk.types.value.AccessToken

actual class AuthorizeViaGoogleUseCase(
    private val signIn: GoogleSignInClient,
    private val lifecycle: Lifecycle
) {
    actual operator fun invoke() {

    }

    actual sealed interface Result {
        @JvmInline
        actual value class Success(actual val accessToken: AccessToken) : Result
        actual object AuthInvalid : Result
    }
}