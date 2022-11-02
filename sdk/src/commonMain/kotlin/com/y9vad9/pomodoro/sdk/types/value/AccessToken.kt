package com.y9vad9.pomodoro.sdk.types.value

import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class AccessToken(public val string: StringValues)