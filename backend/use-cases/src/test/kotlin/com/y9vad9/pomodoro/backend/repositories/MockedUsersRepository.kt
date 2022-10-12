package com.y9vad9.pomodoro.backend.repositories

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.UserName

class MockedUsersRepository : UsersRepository {
    private val users: MutableList<UsersRepository.User> = mutableListOf()

    override suspend fun createUser(userName: UserName, creationTime: DateTime): UsersRepository.UserId {
        users += UsersRepository.User(UsersRepository.UserId(users.size), userName)
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