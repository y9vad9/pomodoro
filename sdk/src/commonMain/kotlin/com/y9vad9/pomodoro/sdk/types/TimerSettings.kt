package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.Milliseconds
import com.y9vad9.pomodoro.sdk.types.value.Regularity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class TimerSettings(
    @SerialName("work_time")
    public val workTime: Milliseconds = Milliseconds(1500000L),
    @SerialName("rest_time")
    public val restTime: Milliseconds = Milliseconds(300000),
    @SerialName("big_rest_time")
    public val bigRestTime: Milliseconds = Milliseconds(600000),
    @SerialName("is_big_rest_enabled")
    public val isBigRestEnabled: Boolean = true,
    @SerialName("big_rest_per")
    public val bigRestPer: Regularity = Regularity(4),
    @SerialName("is_everyone_can_pause")
    public val isEveryoneCanPause: Boolean = false
) {
    @Serializable
    public class Patch(
        @SerialName("work_time")
        public val workTime: Long? = null,
        @SerialName("rest_time")
        public val restTime: Long? = null,
        @SerialName("big_rest_time")
        public val bigRestTime: Long? = null,
        @SerialName("is_big_rest_enabled")
        public val isBigRestEnabled: Boolean? = null,
        @SerialName("big_rest_per")
        public val bigRestPer: Int? = null,
        @SerialName("is_everyone_can_pause")
        public val isEveryoneCanPause: Boolean? = null
    )
}