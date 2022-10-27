package com.y9vad9.pomodoro.backend.application.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ok() {
    get("ok") {
        call.respond("ok")
    }
}