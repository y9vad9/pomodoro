package com.y9vad9.pomodoro.sdk.test

import com.y9vad9.pomodoro.backend.application.routes.setupRoutesWithDatabase
import com.y9vad9.pomodoro.backend.application.startServer
import com.y9vad9.pomodoro.sdk.PomodoroClient
import com.y9vad9.pomodoro.sdk.results.*
import com.y9vad9.pomodoro.sdk.types.TimerSettings
import com.y9vad9.pomodoro.sdk.types.value.AccessToken
import com.y9vad9.pomodoro.sdk.types.value.Code
import com.y9vad9.pomodoro.sdk.types.value.Count
import com.y9vad9.pomodoro.sdk.types.value.Name
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.platform.commons.annotation.Testable
import kotlin.properties.Delegates

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testable
class PomodoroClientTest {
    private val PORT = 9090
    private val client = PomodoroClient("http://0.0.0.0:9090")
    var accessToken: AccessToken by Delegates.notNull()

    @BeforeAll
    fun setup() {
        runBlocking {
            var isServerStarted = false
            startServer(
                PORT,
                false,
                onSetupFinished = { isServerStarted = true }
            ) {
                setupRoutesWithDatabase(
                    Database.connect(
                        "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver"
                    ),
                    FakeGoogleClient()
                )
            }

            while (!isServerStarted)
                yield()

            val result = client.authViaGoogle(
                Code("12345FDC")
            )
            assert(
                result is SignWithGoogleResult.Success
            )
            result as SignWithGoogleResult.Success
            accessToken = result.accessToken
        }
    }

    @Test
    fun okTest(): Unit = runBlocking {
        assert(client.ok())
    }

    @Test
    fun createTimerTest(): Unit = runBlocking {
        assert(
            client.createTimer(
                accessToken,
                Name("Test"),
                TimerSettings()
            ) is CreateTimerResult.Success
        )
    }

    @Test
    fun getTimerTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        assert(
            client.getTimer(
                accessToken,
                creationResult.timerId
            ) is GetTimerResult.Success
        )
    }

    @Test
    fun getTimersTest(): Unit = runBlocking {
        client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        )

        val result = client.getTimers(accessToken)
        assert(result is GetTimersResult.Success)
        result as GetTimersResult.Success
        assert(result.list.isNotEmpty())
    }

    @Test
    fun removeTimerTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        val result = client.removeTimer(accessToken, creationResult.timerId)
        assert(result is RemoveTimerResult.Success)
    }

    @Test
    fun setTimerSettingsTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        val result = client.setTimerSettings(
            accessToken, creationResult.timerId, TimerSettings.Patch(
                bigRestEnabled = false
            )
        )
        assert(result is SetTimerSettingsResult.Success)
        assert(
            !(client.getTimer(accessToken, creationResult.timerId)
                as GetTimerResult.Success).timer.settings.bigRestEnabled
        )
    }

    @Test
    fun startTimerTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        val result = client.startTimer(
            accessToken, creationResult.timerId
        )
        assert(result is StartTimerResult.Success)
    }

    @Test
    fun stopTimerTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        val result = client.stopTimer(
            accessToken, creationResult.timerId
        )
        assert(result is StopTimerResult.Success)
    }

    @Test
    fun createInviteTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        val result = client.createInvite(
            accessToken, creationResult.timerId, Count(5)
        )
        assert(result is CreateInviteResult.Success)
    }

    @Test
    fun removeInviteTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        val createInviteResult = client.createInvite(
            accessToken, creationResult.timerId, Count(5)
        ) as CreateInviteResult.Success

        assert(
            client.removeInvite(
                accessToken, createInviteResult.code
            ) is RemoveInviteResult.Success
        )
    }

    @Test
    fun getLastEventsTest(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

        client.startTimer(accessToken, creationResult.timerId)
        client.stopTimer(accessToken, creationResult.timerId)
        client.startTimer(accessToken, creationResult.timerId)

        val result = client.getLastEvents(
            accessToken, creationResult.timerId, 0..99
        )
        assert(result is GetLastEventsResult.Success)
        result as GetLastEventsResult.Success
        assert(result.list.size == 3)
    }
}