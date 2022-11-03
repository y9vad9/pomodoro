package com.y9vad9.pomodoro.backend.application.types.value

import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class UserId(val int: Int)

fun UsersRepository.UserId.serializable(): UserId = UserId(int)