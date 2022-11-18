package com.y9vad9.pomodoro.backend.application.types.serializer

import com.y9vad9.pomodoro.backend.application.types.TimerUpdate
import com.y9vad9.pomodoro.backend.application.types.TimerUpdateCommand
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

internal val TypesSerializersModule = SerializersModule {
    polymorphic(TimerUpdateCommand::class) {
        subclass(TimerUpdateCommand.Start::class)
        subclass(TimerUpdateCommand.Stop::class)
    }
    polymorphic(TimerUpdate::class) {
        subclass(TimerUpdate.TimerStarted::class)
        subclass(TimerUpdate.TimerStopped::class)
    }
}