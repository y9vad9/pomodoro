package com.y9vad9.pomodoro.backend.usecases.auth

import com.y9vad9.pomodoro.backend.domain.UserName
import com.y9vad9.pomodoro.backend.google.auth.GoogleClient
import com.y9vad9.pomodoro.backend.provider.AccessTokenProvider
import com.y9vad9.pomodoro.backend.provider.CurrentTimeProvider
import com.y9vad9.pomodoro.backend.provider.RefreshTokenProvider
import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.LinkedSocialsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository

class AuthViaGoogleUseCase(
    private val linkedSocials: LinkedSocialsRepository,
    private val users: UsersRepository,
    private val tokensProvider: AccessTokenProvider,
    private val authorizations: AuthorizationsRepository,
    private val time: CurrentTimeProvider,
    private val refreshTokens: RefreshTokenProvider,
    private val googleClient: GoogleClient
) {
    suspend operator fun invoke(code: String): Result {
        val accessTokenResult = googleClient.getAccessToken(code, "about:blank")
            ?: return Result.InvalidAuthorization
        val user = googleClient.getUserProfile(accessTokenResult)
        val linked = linkedSocials.getBySocial(
            LinkedSocialsRepository.Social.Google(
                accessTokenResult.id,
                user.email
            )
        )

        return if (linked == null) {
            val id = users.createUser(UserName(user.name), time.provide())
            val accessToken = tokensProvider.provide()
            authorizations.create(
                id, accessToken, refreshTokens.provide(), time.provide()
            )
            Result.Success(accessToken)
        } else {
            val accessToken = tokensProvider.provide()
            authorizations.create(
                linked,
                tokensProvider.provide(),
                refreshTokens.provide(),
                time.provide()
            )
            Result.Success(accessToken)
        }
    }

    sealed interface Result {
        object InvalidAuthorization : Result

        @JvmInline
        value class Success(val accessToken: AuthorizationsRepository.AccessToken) : Result
    }
}