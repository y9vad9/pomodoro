package com.y9vad9.pomodoro.backend.application.plugins

import com.y9vad9.pomodoro.backend.repositories.AuthorizationsRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.usecases.auth.GetUserIdByAccessTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

suspend inline fun PipelineContext<Unit, ApplicationCall>.authorized(
    block: (UsersRepository.UserId) -> Unit
) {
    val token = call.request.authorization()
    if (token == null)
        call.respond(HttpStatusCode.Unauthorized)
    else {
        val plugin: AuthorizationPlugin =
            call.application.attributes[AuthorizationPlugin.key]

        val userId = plugin.authorized(token)
        if (userId == null)
            call.respond(HttpStatusCode.Unauthorized)
        else block(userId)
    }
}

class AuthorizationPlugin(private val configuration: Configuration) {
    class Configuration {
        lateinit var authorize: GetUserIdByAccessTokenUseCase
    }

    suspend fun authorized(accessToken: String): UsersRepository.UserId? {
        return (configuration.authorize(
            AuthorizationsRepository.AccessToken(accessToken)
        ) as? GetUserIdByAccessTokenUseCase.Result.Success)?.userId
    }

    companion object : Plugin<ApplicationCallPipeline, Configuration, AuthorizationPlugin> {
        override val key: AttributeKey<AuthorizationPlugin> =
            AttributeKey("AuthorizationPlugin")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): AuthorizationPlugin {
            val configuration = Configuration().apply(configure)
            return AuthorizationPlugin(configuration)
        }

    }
}