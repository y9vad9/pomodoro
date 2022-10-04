package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.entity.UserName

interface UsersRepository {
    suspend fun createUser(userName: UserName): UserId
    suspend fun getUser(userId: UserId): User?

    class User(
        val userId: UserId,
        val userName: UserName
    )

    @JvmInline
    value class UserId(val int: Int)
}