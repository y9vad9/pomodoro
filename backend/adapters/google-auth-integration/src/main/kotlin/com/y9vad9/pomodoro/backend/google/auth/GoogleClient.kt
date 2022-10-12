package com.y9vad9.pomodoro.backend.google.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GoogleClient(
    private val clientId: String,
    private val clientSecret: String
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun getAccessToken(code: String, redirectUrl: String): GetAccessTokenResponse? {
        val response = client.post("https://accounts.google.com/o/oauth2/token") {
            parameter("client_id", clientId)
            parameter("client_secret", clientSecret)
            parameter("redirect_uri", redirectUrl)
            parameter("code", code)
            parameter("grant_type", "authorization_code")
        }

        return if (response.status.isSuccess())
            response.body()
        else null
    }

    suspend fun getUserProfile(getAccessTokenResponse: GetAccessTokenResponse): UserProfile {
        val response = client.get("https://www.googleapis.com/oauth2/v1/userinfo") {
            parameter("access_token", getAccessTokenResponse.accessToken)
            parameter("id_token", getAccessTokenResponse.idToken)
            parameter("expires_in", 3599)
            parameter("token_type", "Bearer")
        }

        return response.body()
    }


    @Serializable
    class UserProfile(
        val email: String,
        val name: String
    )

    @Serializable
    class GetAccessTokenResponse(
        @SerialName("access_token")
        val accessToken: String,
        @SerialName("id_token")
        val idToken: String,
        val id: Long
    )
}