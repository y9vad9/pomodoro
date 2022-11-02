package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.Code
import com.y9vad9.pomodoro.sdk.types.value.Count
import kotlinx.serialization.Serializable

@Serializable
public class Invite(
    public val placesLeft: Count,
    public val code: Code
)