package com.y9vad9.pomodoro.backend.application.routes.timer

import com.y9vad9.pomodoro.backend.application.plugins.authorized
import com.y9vad9.pomodoro.backend.application.types.TimerUpdateCommand
import com.y9vad9.pomodoro.backend.application.types.internal
import com.y9vad9.pomodoro.backend.application.types.serializable
import com.y9vad9.pomodoro.backend.application.types.value.TimerId
import com.y9vad9.pomodoro.backend.application.types.value.internal
import com.y9vad9.pomodoro.backend.usecases.timers.GetLiveUpdatesUseCase
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

fun Route.timerUpdates(getLiveUpdates: GetLiveUpdatesUseCase) {
    webSocket("/track", protocol = HttpMethod.Get.value) {
        try {
            authorized { userId ->
                val timerId = receiveDeserialized<TimerId>()

                val result = getLiveUpdates(
                    userId,
                    timerId.internal(),
                    flow {
                        while (isActive)
                            emit(receiveDeserialized<TimerUpdateCommand>().internal())
                    },
                    CoroutineScope(currentCoroutineContext() + Job())
                )

                if (result is GetLiveUpdatesUseCase.Result.NoAccess) {
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No access"))
                    return@webSocket
                }

                result as GetLiveUpdatesUseCase.Result.Success
                launch {
                    result.flow.collectLatest {
                        sendSerialized(it.serializable())
                    }
                }
            }
        } catch (_: Exception) {

        }
    }
}
