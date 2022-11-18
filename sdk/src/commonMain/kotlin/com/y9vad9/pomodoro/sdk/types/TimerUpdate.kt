package com.y9vad9.pomodoro.sdk.types

import kotlinx.serialization.Serializable

public sealed interface TimerUpdate {
    /**
     * Timer events update (start/pause).
     * @param event new event
     */
    @Serializable
    public class EventsUpdate(public val event: TimerEvent) : TimerUpdate

    /**
     * Timer's start confirmation update.
     * Marks that user should confirm start of timer.
     */
    @Serializable
    public object ConfirmationUpdate : TimerUpdate
}