package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.Milliseconds
import com.y9vad9.pomodoro.sdk.types.value.Regularity
import kotlinx.serialization.Serializable

@Serializable
public data class TimerSettings(
    public val workTime: Milliseconds = Milliseconds(1500000L),
    public val restTime: Milliseconds = Milliseconds(300000),
    public val bigRestTime: Milliseconds = Milliseconds(600000),
    public val bigRestEnabled: Boolean = true,
    public val bigRestPer: Regularity = Regularity(4),
    public val isEveryoneCanPause: Boolean = false
) {
    @Serializable
    public class Patch(
        public val workTime: Long? = null,
        public val restTime: Long? = null,
        public val bigRestTime: Long? = null,
        public val bigRestEnabled: Boolean? = null,
        public val bigRestPer: Int? = null,
        public val isEveryoneCanPause: Boolean? = null
    )
}