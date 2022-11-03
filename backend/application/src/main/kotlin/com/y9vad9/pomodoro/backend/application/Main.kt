package com.y9vad9.pomodoro.backend.application

import com.y9vad9.pomodoro.backend.application.routes.setupRoutes
import com.y9vad9.pomodoro.backend.application.routes.setupRoutesWithDatabase
import com.y9vad9.pomodoro.backend.application.types.serializer.jsonModule
import com.y9vad9.pomodoro.backend.application.validator.timerSettingsValidator
import com.y9vad9.pomodoro.backend.google.auth.HttpGoogleClient
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level
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

    val googleClient = HttpGoogleClient(
        System.getenv("GOOGLE_CLIENT_ID"),
        System.getenv("GOOGLE_CLIENT_SECRET")
    )

    startServer(port) {
        setupRoutesWithDatabase(database, googleClient)
    }
}

/**
 * Starts server with default configuration.
 * @param port port on which server will run.
 * @param routingBlock routing customization.
 * @see setupRoutes
 * @see setupRoutesWithDatabase
 */
fun startServer(
    port: Int,
    wait: Boolean = true,
    onSetupFinished: () -> Unit = {},
    routingBlock: Routing.() -> Unit
) {
    embeddedServer(CIO, port) {
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
                serializersModule = jsonModule
            })
        }

        install(RequestValidation) {
            timerSettingsValidator()
        }

        install(CallLogging) {
            level = Level.INFO
            filter { call -> call.request.path().startsWith("/") }
        }

        install(StatusPages) {
            exception<RequestValidationException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, cause.reasons)
            }
            exception<Throwable> { call, throwable ->
                call.respond(
                    HttpStatusCode.InternalServerError, throwable.stackTraceToString()
                )
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

        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = true
        }

        environment.monitor.subscribe(ApplicationStarted) {
            onSetupFinished()
        }

        routing {
            routingBlock()
        }
    }.start(wait = wait)
}