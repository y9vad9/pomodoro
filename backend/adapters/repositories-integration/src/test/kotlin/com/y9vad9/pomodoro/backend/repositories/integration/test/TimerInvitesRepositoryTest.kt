package com.y9vad9.pomodoro.backend.repositories.integration.test

import com.y9vad9.pomodoro.backend.domain.DateTime
import com.y9vad9.pomodoro.backend.domain.TimerName
import com.y9vad9.pomodoro.backend.repositories.*
import com.y9vad9.pomodoro.backend.repositories.TimerInvitesRepository.*
import com.y9vad9.pomodoro.backend.repositories.TimersRepository.Settings
import com.y9vad9.pomodoro.backend.repositories.TimersRepository.TimerId
import com.y9vad9.pomodoro.backend.repositories.UsersRepository
import com.y9vad9.pomodoro.backend.repositories.integration.*
import com.y9vad9.pomodoro.backend.repositories.integration.TimerInvitesRepository
import com.y9vad9.pomodoro.backend.repositories.integration.TimersRepository
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.TimerInvitesDataSource
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.TimersDatabaseDataSource
import com.y9vad9.pomodoro.backend.repositories.integration.datasource.UsersDatabaseDataSource
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.platform.commons.annotation.Testable
import kotlin.properties.Delegates

@Testable
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimerInvitesRepositoryTest {
    private val database = Database.connect(
        "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver"
    )
    private var timerId: TimerId by Delegates.notNull()

    private val users = UsersDatabaseDataSource(database)
    private val timers = TimersRepository(TimersDatabaseDataSource(database))
    private val invitesDs = TimerInvitesDataSource(database)
    private val invites = TimerInvitesRepository(invitesDs)

    @BeforeAll
    fun createInvite(): Unit = runBlocking {
        val userId = users.createUser("User", System.currentTimeMillis())
        val timerId = timers.createTimer(
            TimerName("Test"),
            Settings.Default,
            UsersRepository.UserId(userId),
            DateTime(System.currentTimeMillis())
        )

        invites.createInvite(
            timerId,
            Code("ABCD123"),
            Limit(2)
        )

        invites.createInvite(
            timerId,
            Code("ABCDF12345"),
            Limit(20)
        )
        this@TimerInvitesRepositoryTest.timerId = timerId
    }

    @Test
    fun testGetInvites(): Unit = runBlocking {
        assert(invites.getInvites(timerId).size == 2)
    }

    @Test
    fun testSetLimit(): Unit = runBlocking {
        val newLimit = Limit(5)
        invites.setInviteLimit(
            Code("ABCDF12345"),
            newLimit
        )
        assert(invites.getInvite(Code("ABCDF12345"))!!.limit == newLimit)
    }

    @Test
    fun testRemove(): Unit = runBlocking {
        invites.removeInvite(Code("ABCD123"))
        assert(invites.getInvite(Code("ABCD123")) == null)
    }

}