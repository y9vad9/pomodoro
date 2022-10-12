package com.y9vad9.pomodoro.backend.repositories.integration.test

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.UserName
import com.y9vad9.pomodoro.backend.repositories.integration.UsersDatabaseDataSource
import com.y9vad9.pomodoro.backend.repositories.integration.UsersRepository
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.platform.commons.annotation.Testable

@Testable
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersRepositoryTest {
    private val database = Database.connect(
        "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver"
    )
    private val usersRepository = UsersRepository(UsersDatabaseDataSource(database))

    @Test
    fun test(): Unit = runBlocking {
        val id = usersRepository.createUser(
            UserName("test"),
            DateTime(System.currentTimeMillis())
        )
        assert(usersRepository.getUser(id) != null)
    }

}