package com.y9vad9.pomodoro.backend.application

import com.y9vad9.pomodoro.backend.application.plugins.AuthorizationPlugin
import com.y9vad9.pomodoro.backend.application.routes.setupRoutes
import com.y9vad9.pomodoro.backend.application.validator.timerSettingsValidator
import com.y9vad9.pomodoro.backend.google.auth.GoogleClient
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import java.time.Duration

fun main(): Unit = runBlocking {
    val port = System.getenv("SERVER_PORT")?.toIntOrNull() ?: 8080
    val databaseUrl = System.getenv("DATABASE_URL")
        ?: error("Please provide a database url")
    val databaseUser = System.getenv("DATABASE_USER") ?: ""
    val databasePassword = System.getenv("DATABASE_PASSWORD") ?: ""

    val database = Database.connect(
        databaseUrl,
        user = databaseUser,
        password = databasePassword
    )

    val googleClient = GoogleClient(
        System.getenv("GOOGLE_CLIENT_ID"),
        System.getenv("GOOGLE_CLIENT_SECRET")
    )

    embeddedServer(CIO, port) {
        install(ContentNegotiation) {
            json()
        }

        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
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

        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Patch)
            allowMethod(HttpMethod.Delete)
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
        }

        routing {
            setupRoutes(database, googleClient)
        }

    }.start(wait = true)
}