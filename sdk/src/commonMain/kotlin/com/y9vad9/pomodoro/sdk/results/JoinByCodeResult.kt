package com.y9vad9.pomodoro.sdk.results

import com.y9vad9.pomodoro.sdk.types.value.TimerId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public sealed interface JoinByCodeResult {
    @SerialName("success")
    @JvmInline
    public value class Success(public val timerId: TimerId) : JoinByCodeResult

    @SerialName("not_found")
    public object NotFound : JoinByCodeResult
}