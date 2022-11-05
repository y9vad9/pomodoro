package com.y9vad9.pomodoro.sdk.types

import com.y9vad9.pomodoro.sdk.types.value.Code
import com.y9vad9.pomodoro.sdk.types.value.Count
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class Invite(
    @SerialName("places_left")
    public val placesLeft: Count,
    public val code: Code
)