package com.y9vad9.pomodoro.backend.application.routes.timer.events

import com.y9vad9.pomodoro.backend.usecases.timers.GetEventUpdatesUseCase
import com.y9vad9.pomodoro.backend.usecases.timers.events.GetLastEventsUseCase
import io.ktor.server.routing.*

fun Route.eventsRoot(
    getLastEventsUseCase: GetLastEventsUseCase,
    getEventUpdatesUseCase: GetEventUpdatesUseCase
) {
    route("events") {
        getLastEvents(getLastEventsUseCase)
        eventUpdates(getEventUpdatesUseCase)
    }
}