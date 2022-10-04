package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.User
import com.y9vad9.pomodoro.backend.domain.entity.UserName

class MockedUsersRepository : UsersRepository {
    private val users: MutableList<User> = mutableListOf()

    override suspend fun createUser(userName: UserName): UsersRepository.UserId {
        users += User(userName)
        return UsersRepository.UserId(users.lastIndex)
    }

    override suspend fun getUser(userId: UsersRepository.UserId): UsersRepository.User? {
        return UsersRepository.User(
            userId,
            users.getOrNull(userId.int)?.userName
                ?: return null
        )
    }
}