package com.y9vad9.pomodoro.backend.application.types.serializer

import com.y9vad9.pomodoro.backend.application.types.TimerEvent
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val TypesSerializersModule = SerializersModule {
    polymorphic(TimerEvent::class) {
        subclass(TimerEvent.Started::class)
        subclass(TimerEvent.Paused::class)
    }
}