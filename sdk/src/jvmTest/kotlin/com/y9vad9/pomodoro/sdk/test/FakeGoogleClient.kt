package com.y9vad9.pomodoro.sdk.test

import com.y9vad9.pomodoro.backend.google.auth.GoogleClient

class FakeGoogleClient : GoogleClient {
    override suspend fun getAccessToken(
        code: String, redirectUrl: String
    ): GoogleClient.GetAccessTokenResponse {
        return GoogleClient.GetAccessTokenResponse(
            "///", "123", 123456
        )
    }

    override suspend fun getUserProfile(
        getAccessTokenResponse: GoogleClient.GetAccessTokenResponse
    ): GoogleClient.UserProfile {
        return GoogleClient.UserProfile(
            "myemail@email.com", "fake"
        )
    }
}