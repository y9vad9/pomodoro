package com.y9vad9.pomodoro.sdk.exceptions

import io.ktor.utils.io.errors.*

public class ConnectionException(message: String) : IOException(message)