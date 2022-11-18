package com.y9vad9.pomodoro.sdk.types.serializer

import com.y9vad9.pomodoro.sdk.types.TimerEvent
import com.y9vad9.pomodoro.sdk.types.TimerUpdate
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val TypesSerializersModule = SerializersModule {
    polymorphic(TimerEvent::class) {
        subclass(TimerEvent.Started::class)
        subclass(TimerEvent.Paused::class)
    }
    polymorphic(TimerUpdate::class) {
        subclass(TimerUpdate.ConfirmationUpdate::class)
        subclass(TimerUpdate.EventsUpdate::class)
    }
}