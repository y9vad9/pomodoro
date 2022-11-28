package com.y9vad9.pomodoro.sdk.test

import com.y9vad9.pomodoro.backend.application.routes.setupRoutesWithDatabase
import com.y9vad9.pomodoro.backend.application.startServer
import com.y9vad9.pomodoro.sdk.PomodoroClient
import com.y9vad9.pomodoro.sdk.results.*
import com.y9vad9.pomodoro.sdk.types.TimerSessionCommand
import com.y9vad9.pomodoro.sdk.types.TimerSettings
import com.y9vad9.pomodoro.sdk.types.TimerUpdate
import com.y9vad9.pomodoro.sdk.types.value.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.properties.Delegates

@Testable
class PomodoroClientTest {
    private val client = PomodoroClient("http://0.0.0.0:9090")
    var accessToken: AccessToken by Delegates.notNull()

    companion object {
        private const val PORT = 9090

        @JvmStatic
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
            }
        }
    }

    @BeforeEach
    fun generateToken(): Unit = runBlocking {
        val result = client.authViaGoogle(
            Code("12345FDC")
        )
        assert(
            result is SignWithGoogleResult.Success
        )
        result as SignWithGoogleResult.Success
        accessToken = result.accessToken
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

        val result = client.getTimers(accessToken, Count(5), Offset(0))
        assert(result is GetTimersResult.Success)
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
                isBigRestEnabled = false
            )
        )
        assert(result is SetTimerSettingsResult.Success)
        assert(
            !(client.getTimer(accessToken, creationResult.timerId)
                as GetTimerResult.Success).timer.settings.isBigRestEnabled
        )
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
    fun getTimerUpdates(): Unit = runBlocking {
        val creationResult = client.createTimer(
            accessToken,
            Name("Test"),
            TimerSettings()
        ) as CreateTimerResult.Success

//        delay(1000000L)
        client.getTimerUpdates(
            accessToken,
            creationResult.timerId,
            flow {
                emit(TimerSessionCommand.StartTimer)
            },
            CoroutineScope(Dispatchers.IO)
        ).first() is TimerUpdate.TimerStarted
    }
}