package com.y9vad9.pomodoro.backend.repositories.integration

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.UserName
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.repositories.UsersRepository as UsersRepositoryContract

class UsersRepository(
    private val databaseDataSource: UsersDatabaseDataSource
) : UsersRepositoryContract {

    override suspend fun createUser(userName: UserName, creationTime: DateTime): UsersRepository.UserId {
        return databaseDataSource.createUser(userName.string, creationTime.long)
            .let { UsersRepository.UserId(it) }
    }

    override suspend fun getUser(userId: UsersRepository.UserId): UsersRepository.User? {
        return databaseDataSource.getUser(userId.int)?.toExternalUser()
    }

    private fun UsersDatabaseDataSource.User.toExternalUser(): UsersRepositoryContract.User {
        return UsersRepositoryContract.User(UsersRepository.UserId(id), UserName(userName))
    }
}