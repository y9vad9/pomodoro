package com.y9vad9.pomodoro.backend.application

import com.y9vad9.pomodoro.backend.application.plugins.AuthorizationPlugin
import com.y9vad9.pomodoro.backend.application.validator.timerSettingsValidator
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    embeddedServer(CIO) {
        install(ContentNegotiation) {
            json()
        }

        install(AuthorizationPlugin) {}

        install(RequestValidation) {
            timerSettingsValidator()
        }

        install(StatusPages) {
            exception<RequestValidationException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause.reasons)
            }
        }

        routing {
            // todo
        }

    }.start(wait = true)
}